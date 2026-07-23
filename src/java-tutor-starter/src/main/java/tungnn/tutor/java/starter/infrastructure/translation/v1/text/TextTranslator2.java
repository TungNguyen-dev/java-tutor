package tungnn.tutor.java.starter.infrastructure.translation.v1.text;

import java.util.List;
import tungnn.tutor.java.starter.infrastructure.translation.v1.shared.LanguageCode;

public interface TextTranslator2 {

  List<TextTranslation> translate(
      List<String> texts, LanguageCode languageCode, TranslateOption option);
}
