package tungnn.tutor.java.starter.infrastructure.translation.v1.document;

import java.nio.file.Path;
import tungnn.tutor.java.starter.infrastructure.translation.v1.shared.LanguageCode;

public record DocumentTranslationRequest(Path documentPath, LanguageCode targetLanguage) {}
