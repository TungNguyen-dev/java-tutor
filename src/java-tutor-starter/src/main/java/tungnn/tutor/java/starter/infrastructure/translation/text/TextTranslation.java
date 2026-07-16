package tungnn.tutor.java.starter.infrastructure.translation.text;

public record TextTranslation(
    String sourceText, String sourceLanguage, String targetText, String targetLanguage) {}
