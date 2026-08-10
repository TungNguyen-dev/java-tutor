package tungnn.tutor.java.starter.infrastructure.translation.v1.text;

import java.util.List;
import java.util.Objects;
import tungnn.tutor.java.starter.infrastructure.translation.v1.shared.LanguageCode;

public record TextTranslationRequest(List<TextUnit> texts, LanguageCode targetLanguage) {

  public TextTranslationRequest {
    Objects.requireNonNull(texts);
    Objects.requireNonNull(targetLanguage);

    if (texts.isEmpty()) {
      throw new IllegalArgumentException("texts must not be empty");
    }
  }

  public record TextUnit(String textId, String text) {}
}
