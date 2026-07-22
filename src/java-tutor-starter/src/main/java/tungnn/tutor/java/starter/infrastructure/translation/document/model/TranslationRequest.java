package tungnn.tutor.java.starter.infrastructure.translation.document.model;

import java.nio.file.Path;
import tungnn.tutor.java.starter.infrastructure.translation.shared.LanguageCode;

public record TranslationRequest(Path docPath, LanguageCode targetLanguage) {}
