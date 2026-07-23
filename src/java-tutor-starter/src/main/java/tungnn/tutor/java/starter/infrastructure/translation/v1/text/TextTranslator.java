package tungnn.tutor.java.starter.infrastructure.translation.v1.text;

import java.util.Collection;
import tungnn.tutor.java.starter.infrastructure.translation.v1.shared.LanguageCode;

public interface TextTranslator {

  Collection<TextTranslation> translate(Collection<String> texts, LanguageCode languageCode);
}
