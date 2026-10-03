package tungnn.tutor.java.tool.translation.domain.document;

import java.util.Objects;
import tungnn.tutor.java.tool.translation.shared.TranslationError;

public sealed interface DocumentTranslationResult
    permits DocumentTranslationResult.Success, DocumentTranslationResult.Failure {

  record Success() implements DocumentTranslationResult {}

  record Failure(TranslationError error) implements DocumentTranslationResult {

    public Failure {
      Objects.requireNonNull(error, "error must not be null");
    }
  }
}
