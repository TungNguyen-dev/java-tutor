package tungnn.tutor.java.tool.translation.core.text.orchestrator;

import java.util.List;
import tungnn.tutor.java.tool.translation.shared.TextReference;

public record TranslationResult(List<Entry> translations) {

  public static TranslationResult empty() {
    return new TranslationResult(List.of());
  }

  public record Entry(TextReference textReference, String translatedText) {}
}
