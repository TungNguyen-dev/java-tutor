package tungnn.tutor.java.tool.translation.core.document;

import java.nio.file.Path;
import tungnn.tutor.java.tool.translation.shared.LanguageCode;

public record DocumentTranslationRequest(Path documentPath, LanguageCode targetLanguage) {}
