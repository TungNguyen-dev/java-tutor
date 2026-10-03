package tungnn.tutor.java.tool.translation.domain.document;

import tungnn.tutor.java.tool.translation.shared.LanguageCode;

public interface DocumentTranslator {

  DocumentTranslationResult translate(Document document, LanguageCode targetLanguage);
}
