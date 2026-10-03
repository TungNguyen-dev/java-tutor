package tungnn.tutor.java.tool.translation_old.core.document;

import java.nio.file.Path;
import tungnn.tutor.java.tool.translation_old.shared.LanguageCode;

public record DocumentTranslationRequest(Path documentPath, LanguageCode targetLanguage) {}
