package tungnn.tutor.java.tool.translation.domain.document;

public record SemanticUnitTranslation(
    String textNodeId, int index, String sourceText, String translatedText) {}
