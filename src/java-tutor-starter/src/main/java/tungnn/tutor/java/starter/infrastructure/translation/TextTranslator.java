package tungnn.tutor.java.starter.infrastructure.translation;

import java.util.List;

public interface TextTranslator {

  List<TextTranslationResponse> translate(List<TextTranslationRequest> list);
}
