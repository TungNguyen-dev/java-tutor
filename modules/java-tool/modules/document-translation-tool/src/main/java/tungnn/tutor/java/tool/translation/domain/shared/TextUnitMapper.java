package tungnn.tutor.java.tool.translation.domain.shared;

import java.util.List;
import tungnn.tutor.java.tool.translation.domain.document.SemanticUnit;

public interface TextUnitMapper {

  TextUnitMapping map(List<SemanticUnit> semanticUnits);
}
