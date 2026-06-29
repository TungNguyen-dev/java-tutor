package tungnn.tutor.java.starter.infrastructure.translation;

public record TextTranslationResponse(
    String translationId,
    String sourceText,
    String sourceLanguage,
    String targetText,
    String targetLanguage) {}
