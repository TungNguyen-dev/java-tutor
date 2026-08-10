package tungnn.tutor.java.tool.dbdiff.model;

import java.time.Instant;
import java.util.List;
import java.util.Map;

public class DiffModels {

  // --- 1. CONFIG & OPTIONS ---
  public record DatabaseConfig(
      String jdbcUrl, String username, String password, String schemaName) {}

  public record DiffOptions(
      List<String> includeTables, // Danh sách bảng cần diff (null/empty = all)
      List<String> excludeTables, // Bảng cần bỏ qua
      int maxDataDiffLimitCount // Giới hạn max row diff report per table (chống OOM)
      ) {
    public static DiffOptions defaults() {
      return new DiffOptions(List.of(), List.of(), 1000);
    }
  }

  // --- 2. SCHEMA STRUCTURE RESULTS ---
  public record ColumnDiff(
      String columnName,
      String diffType, // ADDED, REMOVED, MODIFIED
      Map<String, String> sourceAttributes, // e.g., {"type": "VARCHAR(255)", "nullable": "false"}
      Map<String, String> targetAttributes // e.g., {"type": "VARCHAR(100)", "nullable": "true"}
      ) {}

  public record TableStructureDiff(
      String tableName,
      String diffType, // ADDED, REMOVED, MODIFIED, MATCHED
      List<ColumnDiff> columnDiffs,
      List<String> addedIndexes,
      List<String> removedIndexes) {}

  public record DiffSchemaStructureResult(
      Instant timestamp, boolean isIdentical, List<TableStructureDiff> tableDiffs) {}

  // --- 3. SCHEMA DATA RESULTS ---
  public record RowChange(
      String changeType, // INSERT, UPDATE, DELETE
      Map<String, Object> primaryKeys, // e.g., {"id": 101}
      Map<String, ValuePair> fieldChanges // e.g., "price" -> ValuePair(source=15.5, target=12.0)
      ) {}

  public record ValuePair(Object sourceValue, Object targetValue) {}

  public record TableDataDiff(
      String tableName,
      boolean isIdentical,
      long missingInTargetCount, // Cần INSERT
      long missingInSourceCount, // Cần DELETE
      long mismatchedRowCount, // Cần UPDATE
      List<RowChange> sampleRowChanges // Mẫu danh sách thay đổi (đã bị giới hạn bởi limit)
      ) {}

  public record DiffSchemaDataResult(
      Instant timestamp, boolean isIdentical, List<TableDataDiff> tableDataDiffs) {}
}
