package tungnn.tutor.java.tool.translation.domain.text;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import tungnn.tutor.java.tool.translation.shared.LanguageCode;

class DummyTextTranslatorTest {

  private DummyTextTranslator translator;

  @BeforeEach
  void setUp() {
    translator = new DummyTextTranslator();
  }

  @Nested
  @DisplayName("Trường hợp thành công (Success cases)")
  class SuccessCases {

    @Test
    @DisplayName("Dịch danh sách các đoạn văn bản thành công")
    void shouldTranslateTextUnitsSuccessfully() {
      // Given
      LanguageCode targetLang = LanguageCode.EN;
      TextUnit unit1 = new TextUnit("id1", "Xin chào");
      TextUnit unit2 = new TextUnit("id2", "Tạm biệt");

      TextTranslationRequest request =
          new TextTranslationRequest.TextBatchTranslationRequest(List.of(unit1, unit2), targetLang);

      // When
      TextTranslationResult result = translator.translate(request);

      // Then
      TextTranslationResult.Success successResult =
          assertInstanceOf(TextTranslationResult.Success.class, result);

      List<TextUnitTranslation> translations = successResult.translations();
      assertEquals(2, translations.size());

      // Kiểm tra bản dịch thứ nhất
      TextUnitTranslation translation1 = translations.getFirst();
      assertEquals("id1", translation1.textId());
      assertEquals("Xin chào", translation1.original());
      assertEquals("[DUMMY - EN] Xin chào", translation1.translation());
      assertEquals(targetLang, translation1.targetLanguage());

      // Kiểm tra bản dịch thứ hai
      TextUnitTranslation translation2 = translations.get(1);
      assertEquals("id2", translation2.textId());
      assertEquals("Tạm biệt", translation2.original());
      assertEquals("[DUMMY - EN] Tạm biệt", translation2.translation());
      assertEquals(targetLang, translation2.targetLanguage());
    }

    @Test
    @DisplayName("Dịch danh sách rỗng vẫn trả về Success")
    void shouldReturnSuccessWhenTextListIsEmpty() {
      // Given
      LanguageCode targetLang = LanguageCode.JA;
      TextTranslationRequest request =
          new TextTranslationRequest.TextBatchTranslationRequest(List.of(), targetLang);

      // When
      TextTranslationResult result = translator.translate(request);

      // Then
      TextTranslationResult.Success successResult =
          assertInstanceOf(TextTranslationResult.Success.class, result);
      assertTrue(successResult.translations().isEmpty());
    }
  }

  @Nested
  @DisplayName("Trường hợp thất bại / Lỗi dữ liệu đầu vào (Failure cases)")
  class FailureCases {

    @Test
    @DisplayName("Trả về Failure khi Request là null")
    void shouldReturnFailureWhenRequestIsNull() {
      // When
      TextTranslationResult result = translator.translate(null);

      // Then
      TextTranslationResult.Failure failureResult =
          assertInstanceOf(TextTranslationResult.Failure.class, result);
      assertTrue(failureResult.error().message().contains("request must not be null"));
    }

    @Test
    @DisplayName("Trả về Failure khi textId bị blank hoặc null")
    void shouldReturnFailureWhenTextIdIsBlank() {
      // Given
      LanguageCode targetLang = LanguageCode.EN;
      TextUnit invalidUnit = new TextUnit("  ", "Nội dung hợp lệ");
      TextTranslationRequest request =
          new TextTranslationRequest.TextBatchTranslationRequest(List.of(invalidUnit), targetLang);

      // When
      TextTranslationResult result = translator.translate(request);

      // Then
      TextTranslationResult.Failure failureResult =
          assertInstanceOf(TextTranslationResult.Failure.class, result);
      assertTrue(failureResult.error().message().contains("textId must not be blank"));
    }

    @Test
    @DisplayName("Trả về Failure khi có trùng lặp textId trong request")
    void shouldReturnFailureWhenDuplicateTextIdExists() {
      // Given
      LanguageCode targetLang = LanguageCode.EN;
      TextUnit unit1 = new TextUnit("same-id", "Đoán 1");
      TextUnit unit2 = new TextUnit("same-id", "Đoán 2");
      TextTranslationRequest request =
          new TextTranslationRequest.TextBatchTranslationRequest(List.of(unit1, unit2), targetLang);

      // When
      TextTranslationResult result = translator.translate(request);

      // Then
      TextTranslationResult.Failure failureResult =
          assertInstanceOf(TextTranslationResult.Failure.class, result);
      assertTrue(failureResult.error().message().contains("Duplicate textId: same-id"));
    }
  }
}
