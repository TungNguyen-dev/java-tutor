package tungnn.tutor.java.starter.infrastructure.translation.v1.text.orchestrator;

import java.util.List;
import tungnn.tutor.java.starter.infrastructure.translation.v1.shared.TextReference;

public interface TextTranslatorOrchestrator {

  TranslationResult translate(List<TextReference> textReferences, TranslationContext context);
}
