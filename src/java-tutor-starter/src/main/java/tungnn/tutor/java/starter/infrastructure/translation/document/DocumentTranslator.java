package tungnn.tutor.java.starter.infrastructure.translation.document;

import java.nio.file.Path;
import tungnn.tutor.java.starter.infrastructure.translation.shared.LanguageCode;

public interface DocumentTranslator {

  Path translateDocument(Path docPath, LanguageCode targetLanguage);
}
