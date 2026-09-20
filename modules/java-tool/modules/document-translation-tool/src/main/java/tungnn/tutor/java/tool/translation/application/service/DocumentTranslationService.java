package tungnn.tutor.java.tool.translation.application.service;

import java.io.InputStream;
import java.io.OutputStream;
import tungnn.tutor.java.tool.translation.shared.LanguageCode;

public interface DocumentTranslationService {

  void translate(InputStream inputStream, OutputStream outputStream, LanguageCode targetLanguage);
}
