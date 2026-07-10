package tungnn.tutor.java.starter.infrastructure.translation;

import java.util.Collection;

public interface TextTranslator {

  Collection<TextTranslationResponse> translate(
      Collection<TextTranslationRequest> translationRequests);
}
