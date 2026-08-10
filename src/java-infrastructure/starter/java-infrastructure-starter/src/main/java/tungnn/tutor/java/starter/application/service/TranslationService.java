package tungnn.tutor.java.starter.application.service;

import java.nio.file.Path;

public interface TranslationService {

  Path translateDocument(Path sourcePath, String languageCode);
}
