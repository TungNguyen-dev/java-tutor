package tungnn.tutor.java.starter.infrastructure.translation.text;

import java.util.Collection;
import tungnn.tutor.java.starter.infrastructure.translation.shared.LanguageCode;

public interface TextTranslator {

  Collection<TextTranslation> translate(Collection<String> texts, LanguageCode languageCode);
}
