package tungnn.tutor.java.starter.infrastructure.translation.v1.text.orchestrator;

import java.util.List;
import tungnn.tutor.java.starter.infrastructure.translation.v1.shared.TextReference;

public record TranslationResult(List<Entry> translations) {

  public static TranslationResult empty() {
    return new TranslationResult(List.of());
  }

  public record Entry(TextReference textReference, String translatedText) {}
}
