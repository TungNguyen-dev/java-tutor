package tungnn.tutor.java.tool.translation.service;

import java.nio.file.Path;

public interface TranslationService {

  Path translateDocument(Path sourcePath, String languageCode);
}
