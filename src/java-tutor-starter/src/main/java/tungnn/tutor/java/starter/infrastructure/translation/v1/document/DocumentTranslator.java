package tungnn.tutor.java.starter.infrastructure.translation.v1.document;

import tungnn.tutor.java.starter.infrastructure.translation.v1.document.model.TranslationRequest;
import tungnn.tutor.java.starter.infrastructure.translation.v1.document.model.TranslationResponse;

public interface DocumentTranslator {

  TranslationResponse translate(TranslationRequest request);
}
