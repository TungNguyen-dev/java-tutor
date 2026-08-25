package tungnn.tutor.java.tool.translation.core.text.orchestrator;

import java.util.List;
import tungnn.tutor.java.tool.translation.shared.TextReference;

public interface TextTranslatorOrchestrator {

  TranslationResult translate(List<TextReference> textReferences, TranslationContext context);
}
