package tungnn.tutor.java.tool.translation.domain.document;

import java.util.List;

public interface TextBlock {

  String textBlockId();

  BlockType blockType();

  List<SemanticUnit> semanticUnits();

  boolean replaceContent(List<SemanticUnitTranslation> translatedUnits);

  enum BlockType {
    PARAGRAPH,
    TABLE_CELL,
    TEXT_BOX,
    SHAPE,
    CAPTION
  }
}
