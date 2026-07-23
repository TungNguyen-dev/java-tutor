package tungnn.tutor.java.starter.infrastructure.translation.v1.document.model;

import java.nio.file.Path;
import tungnn.tutor.java.starter.infrastructure.translation.v1.shared.LanguageCode;

public record TranslationRequest(Path docPath, LanguageCode targetLanguage) {}
