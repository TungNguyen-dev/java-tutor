package tungnn.tutor.java.tool.dbdiff.core;

import java.util.stream.Stream;
import tungnn.tutor.java.tool.dbdiff.model.DatabaseConfig;
import tungnn.tutor.java.tool.dbdiff.model.SchemaDataDiffOption;
import tungnn.tutor.java.tool.dbdiff.model.SchemaDataDiffs;
import tungnn.tutor.java.tool.dbdiff.model.SchemaStructureDiffs;

public interface DatabaseDiffService {

  SchemaStructureDiffs diffSchemaStructure(DatabaseConfig reference, DatabaseConfig target);

  SchemaDataDiffs diffSchemaData(
      DatabaseConfig reference, DatabaseConfig target, SchemaDataDiffOption option);

  Stream<SchemaDataDiffs.TableRowDataDiff> streamSchemaDataDiff(
      DatabaseConfig reference, DatabaseConfig target, SchemaDataDiffOption option);
}
