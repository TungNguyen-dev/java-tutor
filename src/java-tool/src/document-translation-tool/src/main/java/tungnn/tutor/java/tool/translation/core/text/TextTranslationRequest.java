package tungnn.tutor.java.tool.translation.core.text;

import java.util.List;
import java.util.Objects;
import tungnn.tutor.java.tool.translation.shared.LanguageCode;

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
