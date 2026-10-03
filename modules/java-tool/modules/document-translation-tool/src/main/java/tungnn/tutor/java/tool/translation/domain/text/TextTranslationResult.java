package tungnn.tutor.java.tool.translation.domain.text;

import java.util.List;
import java.util.Objects;
import tungnn.tutor.java.tool.translation.shared.TranslationError;

public sealed interface TextTranslationResult
    permits TextTranslationResult.Success, TextTranslationResult.Failure {

  record Success(List<TextUnitTranslation> translations) implements TextTranslationResult {

    public Success {
      Objects.requireNonNull(translations, "translations must not be null");

      translations = List.copyOf(translations);
    }
  }

  record Failure(TranslationError error) implements TextTranslationResult {

    public Failure {
      Objects.requireNonNull(error, "error must not be null");
    }
  }
}
