package tungnn.tutor.java.starter.infrastructure.translation.v2.document.model;

import java.nio.file.Path;
import tungnn.tutor.java.starter.infrastructure.translation.v2.shared.TranslationLanguage;

public record DocumentTranslationRequest(Path documentPath, TranslationLanguage targetLanguage) {}
