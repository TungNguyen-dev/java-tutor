package tungnn.tutor.java.starter.infrastructure.translation.document;

import tungnn.tutor.java.starter.infrastructure.translation.document.model.TranslationRequest;
import tungnn.tutor.java.starter.infrastructure.translation.document.model.TranslationResponse;

public interface DocumentTranslator {

  TranslationResponse translate(TranslationRequest request);
}
