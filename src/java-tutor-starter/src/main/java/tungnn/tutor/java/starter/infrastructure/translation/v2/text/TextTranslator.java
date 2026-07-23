package tungnn.tutor.java.starter.infrastructure.translation.v2.text;

import java.util.List;
import tungnn.tutor.java.starter.infrastructure.translation.v2.text.model.TextTranslationRequest;
import tungnn.tutor.java.starter.infrastructure.translation.v2.text.model.TextTranslationResponse;

public interface TextTranslator {

  List<TextTranslationResponse> translate(List<TextTranslationRequest> requests);
}
