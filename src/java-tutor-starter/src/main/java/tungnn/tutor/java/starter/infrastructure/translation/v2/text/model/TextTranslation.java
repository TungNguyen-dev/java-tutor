package tungnn.tutor.java.starter.infrastructure.translation.v2.text.model;

import tungnn.tutor.java.starter.infrastructure.translation.v2.shared.TranslationLanguage;

public record TextTranslation(
    String sourceText,
    TranslationLanguage sourceLanguage,
    String targetText,
    TranslationLanguage targetLanguage) {}
