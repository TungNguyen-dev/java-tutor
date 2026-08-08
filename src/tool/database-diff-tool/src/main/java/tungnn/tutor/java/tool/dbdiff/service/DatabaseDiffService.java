package tungnn.tutor.java.tool.dbdiff.service;

import tungnn.tutor.java.tool.dbdiff.model.DiffModels.DatabaseConfig;
import tungnn.tutor.java.tool.dbdiff.model.DiffModels.DiffOptions;
import tungnn.tutor.java.tool.dbdiff.model.DiffModels.DiffSchemaDataResult;
import tungnn.tutor.java.tool.dbdiff.model.DiffModels.DiffSchemaStructureResult;

public interface DatabaseDiffService {

  /** So sánh cấu trúc (Schema Structure) giữa 2 Database với option mặc định. */
  DiffSchemaStructureResult diffSchemaStructure(DatabaseConfig source, DatabaseConfig target);

  /** So sánh cấu trúc với các tùy chọn filter/limit cụ thể. */
  DiffSchemaStructureResult diffSchemaStructure(
      DatabaseConfig source, DatabaseConfig target, DiffOptions options);

  /** So sánh dữ liệu (Schema Data) theo dạng Streaming/Two-pointer với option mặc định. */
  DiffSchemaDataResult diffSchemaData(DatabaseConfig source, DatabaseConfig target);

  /** So sánh dữ liệu với các tùy chọn filter/limit cụ thể. */
  DiffSchemaDataResult diffSchemaData(
      DatabaseConfig source, DatabaseConfig target, DiffOptions options);

  /** Convenience method: Thực hiện so sánh toàn bộ cả Structure và Data trong 1 lượt. */
  default FullDiffResult diffAll(
      DatabaseConfig source, DatabaseConfig target, DiffOptions options) {
    var structureResult = diffSchemaStructure(source, target, options);
    var dataResult = diffSchemaData(source, target, options);
    return new FullDiffResult(structureResult, dataResult);
  }

  record FullDiffResult(
      DiffSchemaStructureResult structureResult, DiffSchemaDataResult dataResult) {
    public boolean isIdentical() {
      return structureResult.isIdentical() && dataResult.isIdentical();
    }
  }
}
