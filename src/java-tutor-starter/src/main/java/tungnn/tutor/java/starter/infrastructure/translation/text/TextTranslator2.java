package tungnn.tutor.java.starter.infrastructure.translation.text;

import java.util.List;
import tungnn.tutor.java.starter.infrastructure.translation.shared.LanguageCode;

public interface TextTranslator2 {

  List<TextTranslation> translate(
      List<String> texts, LanguageCode languageCode, TranslateOption option);
}
