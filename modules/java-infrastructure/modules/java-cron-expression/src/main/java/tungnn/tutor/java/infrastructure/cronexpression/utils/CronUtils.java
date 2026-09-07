package tungnn.tutor.java.infrastructure.cronexpression.utils;

import static com.cronutils.model.field.expression.FieldExpressionFactory.and;
import static com.cronutils.model.field.expression.FieldExpressionFactory.between;
import static com.cronutils.model.field.expression.FieldExpressionFactory.on;
import static com.cronutils.model.field.expression.FieldExpressionFactory.questionMark;

import com.cronutils.builder.CronBuilder;
import com.cronutils.descriptor.CronDescriptor;
import com.cronutils.mapper.CronMapper;
import com.cronutils.model.Cron;
import com.cronutils.model.CronType;
import com.cronutils.model.definition.CronDefinitionBuilder;
import com.cronutils.model.field.CronFieldName;
import com.cronutils.model.field.expression.FieldExpression;
import com.cronutils.model.time.ExecutionTime;
import com.cronutils.parser.CronParser;
import java.time.Duration;
import java.time.LocalDate;
import java.time.Year;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;

public final class CronUtils {

  private CronUtils() {
    // Utility class
  }

  // 1. PARSE & VALIDATE
  public static Cron parseAndValidate(String cronExpression, CronType cronType) {
    CronParser parser = new CronParser(CronDefinitionBuilder.instanceDefinitionFor(cronType));
    Cron cron = parser.parse(cronExpression);
    cron.validate();
    return cron;
  }

  public static boolean isValid(String cronExpression, CronType cronType) {
    try {
      parseAndValidate(cronExpression, cronType);
      return true;
    } catch (IllegalArgumentException e) {
      return false;
    }
  }

  // 2. DESCRIBE
  public static String describe(String cronExpression, CronType cronType, Locale locale) {
    Cron cron = parseAndValidate(cronExpression, cronType);
    return CronDescriptor.instance(locale).describe(cron);
  }

  public static String describe(String cronExpression, CronType cronType) {
    return describe(cronExpression, cronType, Locale.getDefault());
  }

  // 3. MIGRATE
  public static String migrate(String cronExpression, CronType sourceType, CronMapper mapper) {
    Cron sourceCron = parseAndValidate(cronExpression, sourceType);
    return mapper.map(sourceCron).asString();
  }

  // 4. EXECUTION TIME CALCULATOR
  public static Optional<ZonedDateTime> getNextExecution(
      String cronExpression, CronType cronType, ZonedDateTime fromTime) {
    Cron cron = parseAndValidate(cronExpression, cronType);
    return ExecutionTime.forCron(cron).nextExecution(fromTime);
  }

  public static Optional<ZonedDateTime> getLastExecution(
      String cronExpression, CronType cronType, ZonedDateTime fromTime) {
    Cron cron = parseAndValidate(cronExpression, cronType);
    return ExecutionTime.forCron(cron).lastExecution(fromTime);
  }

  public static Optional<Duration> getTimeToNextExecution(
      String cronExpression, CronType cronType, ZonedDateTime fromTime) {
    Cron cron = parseAndValidate(cronExpression, cronType);
    return ExecutionTime.forCron(cron).timeToNextExecution(fromTime);
  }

  public static Optional<Duration> getTimeFromLastExecution(
      String cronExpression, CronType cronType, ZonedDateTime fromTime) {
    Cron cron = parseAndValidate(cronExpression, cronType);
    return ExecutionTime.forCron(cron).timeFromLastExecution(fromTime);
  }

  // 5. COMPLEX

  /**
   * Generates a Cron expression based on a list of date ranges (Day of Month) and applies date
   * overrides. Fully compatible with {@code cron-utils 9.2.1}.
   *
   * @param cronExpression the base Cron expression string containing time fields (second, minute,
   *     hour)
   * @param cronType the target {@link CronType} syntax rules (e.g., QUARTZ, SPRING, UNIX)
   * @param dateRanges map of start and end dates representing target execution periods within a
   *     single month
   * @param dateOverrides map of original day-of-month to overridden day-of-month values
   * @return the generated Cron expression as a formatted String
   * @throws IllegalArgumentException if {@code dateRanges} is null/empty, dates are out of order,
   *     or span multiple months
   */
  public static String generateCronForDateRanges(
      String cronExpression,
      CronType cronType,
      Map<LocalDate, LocalDate> dateRanges,
      Map<Integer, Integer> dateOverrides) {

    // 1. Validate date ranges and extract target month safely
    var targetMonth = extractAndValidateTargetMonth(dateRanges);
    var currentYear = Year.now().getValue();

    // 2. Parse & Validate the base Cron expression
    var baseCron = parseAndValidate(cronExpression, cronType);

    // 3. Process date overrides and group Day-of-Month expressions
    List<FieldExpression> domExpressions = new ArrayList<>();

    for (var entry : dateRanges.entrySet()) {
      var startDay = entry.getKey().getDayOfMonth();
      var endDay = entry.getValue().getDayOfMonth();

      if (startDay == endDay) {
        var effectiveDay =
            (dateOverrides != null && dateOverrides.containsKey(startDay))
                ? dateOverrides.get(startDay)
                : startDay;
        domExpressions.add(on(effectiveDay));
      } else {
        var hasOverrideInBetween = false;
        if (dateOverrides != null) {
          for (var d = startDay; d <= endDay; d++) {
            if (dateOverrides.containsKey(d)) {
              hasOverrideInBetween = true;
              break;
            }
          }
        }

        if (!hasOverrideInBetween) {
          domExpressions.add(between(startDay, endDay));
        } else {
          for (var d = startDay; d <= endDay; d++) {
            var effectiveDay = dateOverrides.getOrDefault(d, d);
            domExpressions.add(on(effectiveDay));
          }
        }
      }
    }

    var finalDomExpression =
        domExpressions.size() == 1 ? domExpressions.getFirst() : and(domExpressions);

    // 4. Use CronBuilder to reconstruct the new Cron object
    var builder = CronBuilder.cron(CronDefinitionBuilder.instanceDefinitionFor(cronType));

    // Copy standard time fields from base cron
    if (baseCron.retrieve(CronFieldName.SECOND) != null) {
      builder.withSecond(baseCron.retrieve(CronFieldName.SECOND).getExpression());
    }
    if (baseCron.retrieve(CronFieldName.MINUTE) != null) {
      builder.withMinute(baseCron.retrieve(CronFieldName.MINUTE).getExpression());
    }
    if (baseCron.retrieve(CronFieldName.HOUR) != null) {
      builder.withHour(baseCron.retrieve(CronFieldName.HOUR).getExpression());
    }

    // Apply new date fields
    builder.withDoM(finalDomExpression);
    builder.withMonth(on(targetMonth));

    // Update YEAR field if supported by cronType (e.g., QUARTZ)
    if (baseCron.retrieve(CronFieldName.YEAR) != null) {
      builder.withYear(on(currentYear));
    }

    // Handle DAY_OF_WEEK logic for QUARTZ / SPRING formats
    if (baseCron.retrieve(CronFieldName.DAY_OF_WEEK) != null) {
      if (cronType == CronType.QUARTZ || cronType == CronType.SPRING) {
        builder.withDoW(questionMark());
      } else {
        builder.withDoW(baseCron.retrieve(CronFieldName.DAY_OF_WEEK).getExpression());
      }
    }

    return builder.instance().asString();
  }

  /**
   * Validates all input date ranges and returns the target month as a primitive {@code int}.
   *
   * @param dateRanges map of start and end dates representing target execution periods
   * @return the target month value (1-12)
   * @throws IllegalArgumentException if {@code dateRanges} is null/empty, dates are out of order,
   *     or span multiple months
   */
  private static int extractAndValidateTargetMonth(Map<LocalDate, LocalDate> dateRanges) {
    if (dateRanges == null || dateRanges.isEmpty()) {
      throw new IllegalArgumentException("dateRanges cannot be null or empty");
    }

    int targetMonth = -1;

    for (var entry : dateRanges.entrySet()) {
      var start = entry.getKey();
      var end = entry.getValue();

      if (start.isAfter(end)) {
        throw new IllegalArgumentException("StartDate must be before or equal to EndDate");
      }

      if (start.getMonthValue() != end.getMonthValue()) {
        throw new IllegalArgumentException("StartDate and EndDate must be in the same month");
      }

      if (targetMonth == -1) {
        targetMonth = start.getMonthValue();
      } else if (targetMonth != start.getMonthValue()) {
        throw new IllegalArgumentException("All date ranges must be within the same month");
      }
    }

    return targetMonth;
  }
}
