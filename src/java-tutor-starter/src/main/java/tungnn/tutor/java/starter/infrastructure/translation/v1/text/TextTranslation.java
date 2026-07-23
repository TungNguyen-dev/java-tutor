package tungnn.tutor.java.starter.infrastructure.translation.v1.text;

public record TextTranslation(
    String sourceText, String sourceLanguage, String targetText, String targetLanguage) {}
