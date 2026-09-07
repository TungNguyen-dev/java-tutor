package tungnn.tutor.java.infrastructure.cronexpression.utils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.cronutils.model.CronType;
import java.time.LocalDate;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class CronUtilsTest {

  private static final String BASE_QUARTZ_CRON = "0 0 12 * * ? *";

  @Nested
  @DisplayName("Tests cho các trường hợp hợp lệ (Happy Path)")
  class SuccessCases {

    @Test
    @DisplayName("Tạo Cron hợp lệ với 1 ngày duy nhất (Không có override)")
    void shouldGenerateCron_WhenSingleDayGiven() {
      // Given
      Map<LocalDate, LocalDate> dateRanges =
          Map.of(LocalDate.of(2026, 10, 15), LocalDate.of(2026, 10, 15));

      // When
      String result =
          CronUtils.generateCronForDateRanges(BASE_QUARTZ_CRON, CronType.QUARTZ, dateRanges, null);

      // Then
      assertEquals("0 0 12 15 10 ? 2026", result);
    }

    @Test
    @DisplayName("Tạo Cron hợp lệ với 1 dải ngày liên tục (Between - Không có override)")
    void shouldGenerateCron_WhenDateRangeGivenWithoutOverrides() {
      // Given
      Map<LocalDate, LocalDate> dateRanges =
          Map.of(LocalDate.of(2026, 10, 1), LocalDate.of(2026, 10, 5));

      // When
      String result =
          CronUtils.generateCronForDateRanges(BASE_QUARTZ_CRON, CronType.QUARTZ, dateRanges, null);

      // Then
      assertEquals("0 0 12 1-5 10 ? 2026", result);
    }

    @Test
    @DisplayName("Tạo Cron hợp lệ khi có override ngày trong dải ngày")
    void shouldGenerateCron_WhenDateRangeAndOverridesGiven() {
      // Given
      Map<LocalDate, LocalDate> dateRanges =
          Map.of(LocalDate.of(2026, 10, 1), LocalDate.of(2026, 10, 5));

      Map<Integer, Integer> overrides = Map.of(3, 8);

      // When
      String result =
          CronUtils.generateCronForDateRanges(
              BASE_QUARTZ_CRON, CronType.QUARTZ, dateRanges, overrides);

      // Then
      assertEquals("0 0 12 1,2,8,4,5 10 ? 2026", result);
    }

    @Test
    @DisplayName("Tạo Cron hợp lệ cho nhiều khoảng ngày (Multiple Ranges)")
    void shouldGenerateCron_WhenMultipleDateRangesGiven() {
      // Given
      Map<LocalDate, LocalDate> dateRanges = new LinkedHashMap<>();
      dateRanges.put(LocalDate.of(2026, 10, 1), LocalDate.of(2026, 10, 3));
      dateRanges.put(LocalDate.of(2026, 10, 20), LocalDate.of(2026, 10, 20));

      // When
      String result =
          CronUtils.generateCronForDateRanges(BASE_QUARTZ_CRON, CronType.QUARTZ, dateRanges, null);

      // Then
      assertEquals("0 0 12 1-3,20 10 ? 2026", result);
    }
  }

  @Nested
  @DisplayName("Tests cho các trường hợp ngoại lệ (Validation Failure Cases)")
  class ExceptionCases {

    @Test
    @DisplayName("Ném ngoại lệ khi dateRanges là null hoặc rỗng")
    void shouldThrowException_WhenDateRangesIsNullOrEmpty() {
      IllegalArgumentException nullException =
          assertThrows(
              IllegalArgumentException.class,
              () ->
                  CronUtils.generateCronForDateRanges(
                      BASE_QUARTZ_CRON, CronType.QUARTZ, null, null));

      assertEquals("dateRanges cannot be null or empty", nullException.getMessage());

      IllegalArgumentException emptyException =
          assertThrows(
              IllegalArgumentException.class,
              () ->
                  CronUtils.generateCronForDateRanges(
                      BASE_QUARTZ_CRON, CronType.QUARTZ, Collections.emptyMap(), null));

      assertEquals("dateRanges cannot be null or empty", emptyException.getMessage());
    }

    @Test
    @DisplayName("Ném ngoại lệ khi StartDate sau EndDate")
    void shouldThrowException_WhenStartDateIsAfterEndDate() {
      Map<LocalDate, LocalDate> invalidRanges =
          Map.of(LocalDate.of(2026, 10, 10), LocalDate.of(2026, 10, 5));

      IllegalArgumentException exception =
          assertThrows(
              IllegalArgumentException.class,
              () ->
                  CronUtils.generateCronForDateRanges(
                      BASE_QUARTZ_CRON, CronType.QUARTZ, invalidRanges, null));

      assertEquals("StartDate must be before or equal to EndDate", exception.getMessage());
    }

    @Test
    @DisplayName("Ném ngoại lệ khi StartDate và EndDate khác tháng trong cùng 1 khoảng")
    void shouldThrowException_WhenStartAndEndInDifferentMonths() {
      Map<LocalDate, LocalDate> invalidRanges =
          Map.of(LocalDate.of(2026, 10, 25), LocalDate.of(2026, 11, 5));

      IllegalArgumentException exception =
          assertThrows(
              IllegalArgumentException.class,
              () ->
                  CronUtils.generateCronForDateRanges(
                      BASE_QUARTZ_CRON, CronType.QUARTZ, invalidRanges, null));

      assertEquals("StartDate and EndDate must be in the same month", exception.getMessage());
    }

    @Test
    @DisplayName("Ném ngoại lệ khi các khoảng ngày nằm ở các tháng khác nhau")
    void shouldThrowException_WhenMultipleRangesHaveDifferentMonths() {
      Map<LocalDate, LocalDate> invalidRanges = new LinkedHashMap<>();
      invalidRanges.put(LocalDate.of(2026, 10, 1), LocalDate.of(2026, 10, 5));
      invalidRanges.put(LocalDate.of(2026, 11, 10), LocalDate.of(2026, 11, 15));

      IllegalArgumentException exception =
          assertThrows(
              IllegalArgumentException.class,
              () ->
                  CronUtils.generateCronForDateRanges(
                      BASE_QUARTZ_CRON, CronType.QUARTZ, invalidRanges, null));

      assertEquals("All date ranges must be within the same month", exception.getMessage());
    }

    @Test
    @DisplayName("Ném ngoại lệ khi chuỗi Cron gốc không hợp lệ")
    void shouldThrowException_WhenBaseCronIsInvalid() {
      String invalidBaseCron = "INVALID CRON STRING";

      Map<LocalDate, LocalDate> dateRanges =
          Map.of(LocalDate.of(2026, 10, 1), LocalDate.of(2026, 10, 5));

      assertThrows(
          IllegalArgumentException.class,
          () ->
              CronUtils.generateCronForDateRanges(
                  invalidBaseCron, CronType.QUARTZ, dateRanges, null));
    }
  }
}
