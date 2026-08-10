package tungnn.tutor.java.tool.dbdiff;

import tungnn.tutor.java.core.lib.jdbc.extractor.DefaultMetadataExtractor;
import tungnn.tutor.java.tool.dbdiff.model.*;
import tungnn.tutor.java.tool.dbdiff.service.impl.DatabaseDiffServiceImpl;

public class Demo {

  public static void main(String[] args) {
    // 1. Khởi tạo extractor và service
    var metadataExtractor = new DefaultMetadataExtractor();
    var databaseDiffService = new DatabaseDiffServiceImpl(metadataExtractor);

    // 2. Cấu hình thông tin kết nối CSDL (Đọc từ Env Vars hoặc fallback về giá trị mặc định)
    String dbUrl = getEnvOrDefault("DB_URL", "jdbc:mysql://localhost:3306");
    String dbUser = getEnvOrDefault("DB_USER", "root");
    String dbPass = getEnvOrDefault("DB_PASS", "root");

    var refDb = new DatabaseConfig(dbUrl, dbUser, dbPass, "HR");
    var targetDb = new DatabaseConfig(dbUrl, dbUser, dbPass, "SALES");

    System.out.println("==================================================");
    System.out.printf(
        "Bắt đầu so sánh Schema [%s] (Reference) vs [%s] (Target)%n",
        refDb.schemaName(), targetDb.schemaName());
    System.out.println("==================================================\n");

    // 3. So sánh Cấu trúc (DDL / Metadata)
    System.out.println(">>> [1/2] Đang so sánh Cấu trúc Schema...");
    SchemaStructureDiffs structureDiff = databaseDiffService.diffSchemaStructure(refDb, targetDb);
    printStructureDiffs(structureDiff);

    // 4. So sánh Dữ liệu (DML / Data)
    System.out.println("\n>>> [2/2] Đang so sánh Dữ liệu...");
    SchemaDataDiffs dataDiff =
        databaseDiffService.diffSchemaData(refDb, targetDb, SchemaDataDiffOption.defaultOption());
    printDataDiffs(dataDiff);

    System.out.println("\nHoàn tất quá trình so sánh!");
  }

  // =========================================================================
  // Output Helpers
  // =========================================================================

  private static void printStructureDiffs(SchemaStructureDiffs diffs) {
    if (!diffs.hasChanges()) {
      System.out.println("-> Cấu trúc CSDL hoàn toàn giống nhau.");
      return;
    }

    System.out.println("-> Phát hiện sự thay đổi về Cấu trúc:");

    // 1. In diffs của Bảng
    for (var tableDiff : diffs.tableDiffs()) {
      if (tableDiff.status() == DiffStatus.UNCHANGED) {
        continue;
      }

      System.out.printf("  [Table: %s] Status: %s%n", tableDiff.tableName(), tableDiff.status());

      // Attribute Changes
      for (var change : tableDiff.tableChanges()) {
        System.out.printf(
            "    - Metadata [%s]: '%s' -> '%s'%n",
            change.attributeName(), change.oldValue(), change.newValue());
      }

      // Column Changes
      for (var col : tableDiff.columnDiffs()) {
        if (col.status() != DiffStatus.UNCHANGED) {
          System.out.printf("    - Column [%s] (%s)%n", col.columnName(), col.status());
          for (var change : col.changes()) {
            System.out.printf(
                "        * %s: '%s' -> '%s'%n",
                change.attributeName(), change.oldValue(), change.newValue());
          }
        }
      }

      // Constraint Changes
      for (var con : tableDiff.constraintDiffs()) {
        if (con.status() != DiffStatus.UNCHANGED) {
          System.out.printf(
              "    - Constraint [%s] Type: %s (%s)%n",
              con.constraintName(), con.constraintType(), con.status());
        }
      }

      // Index Changes
      for (var idx : tableDiff.indexDiffs()) {
        if (idx.status() != DiffStatus.UNCHANGED) {
          System.out.printf("    - Index [%s] (%s)%n", idx.indexName(), idx.status());
        }
      }
    }

    // 2. In diffs của Programmable Objects (Views, Procedures, Functions...)
    printObjectDiffs("View", diffs.viewDiffs());
    printObjectDiffs("Procedure", diffs.procedureDiffs());
    printObjectDiffs("Function", diffs.functionDiffs());
    printObjectDiffs("Trigger", diffs.triggerDiffs());
    printObjectDiffs("Sequence", diffs.sequenceDiffs());
    printObjectDiffs("CustomType", diffs.customTypeDiffs());
  }

  private static void printObjectDiffs(
      String typeLabel, java.util.List<SchemaStructureDiffs.ObjectStructureDiff> objectDiffs) {
    for (var obj : objectDiffs) {
      if (obj.status() != DiffStatus.UNCHANGED) {
        System.out.printf("  [%s: %s] Status: %s%n", typeLabel, obj.objectName(), obj.status());
      }
    }
  }

  private static void printDataDiffs(SchemaDataDiffs diffs) {
    if (!diffs.hasChanges()) {
      System.out.println("-> Dữ liệu giữa các bảng hoàn toàn trùng khớp.");
      return;
    }

    System.out.println("-> Phát hiện sự thay đổi về Dữ liệu:");
    for (var tableData : diffs.tableDataDiffs()) {
      if (tableData.hasChanges()) {
        System.out.printf(
            "  [Table Data: %s] Added: %d | Modified: %d | Removed: %d%n",
            tableData.tableName(),
            tableData.addedCount(),
            tableData.modifiedCount(),
            tableData.removedCount());

        for (var row : tableData.rowDiffs()) {
          System.out.printf("    - Key %s | Status: %s%n", row.identityKey(), row.status());
          for (var change : row.columnChanges()) {
            System.out.printf(
                "        * Column [%s]: '%s' -> '%s'%n",
                change.columnName(), change.oldValue(), change.newValue());
          }
        }
      }
    }
  }

  private static String getEnvOrDefault(String name, String defaultValue) {
    String val = System.getenv(name);
    return (val != null && !val.isBlank()) ? val : defaultValue;
  }
}
