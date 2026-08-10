package tungnn.tutor.java.tool.dbdiff.sample;

import tungnn.tutor.java.core.lib.jdbc.extractor.DefaultSchemaExtractor;
import tungnn.tutor.java.tool.dbdiff.model.DiffModels;
import tungnn.tutor.java.tool.dbdiff.service.DatabaseDiffService;
import tungnn.tutor.java.tool.dbdiff.service.impl.DatabaseDiffServiceImpl;

public class Demo {

  public static void main(String[] args) {
    var schemaExtractor = new DefaultSchemaExtractor();
    var databaseDiffService = new DatabaseDiffServiceImpl(schemaExtractor);
    var diffOptions = DiffModels.DiffOptions.defaults();

    // Lấy thông tin kết nối từ Environment Variables
    String url =
        System.getenv().getOrDefault("DB_URL", "jdbc:postgresql://localhost:5432/postgres");
    String user = System.getenv().getOrDefault("DB_USER", "postgres");
    String password = System.getenv().getOrDefault("DB_PASS", "postgres");

    // Khai báo cấu hình Source (schema HR) và Target (schema SALES)
    var source = new DiffModels.DatabaseConfig(url, user, password, "HR");
    var target = new DiffModels.DatabaseConfig(url, user, password, "SALES");

    System.out.println("=== BẮT ĐẦU SO SÁNH SCHEMA STRUCTURE VÀ DATA ===");
    System.out.printf(
        "Source Schema: %s | Target Schema: %s%n", source.schemaName(), target.schemaName());
    System.out.println("------------------------------------------------");

    // Thực hiện so sánh toàn bộ Structure & Data
    DatabaseDiffService.FullDiffResult diffResult =
        databaseDiffService.diffAll(source, target, diffOptions);

    // Dynamic output kết quả
    printResult(diffResult);
  }

  private static void printResult(DatabaseDiffService.FullDiffResult diffResult) {
    System.out.printf(
        "%n=== KẾT QUẢ TỔNG QUAN: %s ===%n%n",
        diffResult.isIdentical()
            ? "HOÀN TOÀN GIỐNG NHAU (IDENTICAL)"
            : "CÓ SỰ KHÁC BIỆT (MISMATCHED)");

    // --- 1. CẤU TRÚC (STRUCTURE DIFF) ---
    var struct = diffResult.structureResult();
    System.out.println("1. CẤU TRÚC (STRUCTURE DIFF):");
    System.out.printf("   - Trạng thái: %s%n", struct.isIdentical() ? "MATCHED" : "DIFFERENT");
    System.out.printf("   - Tóm tắt bảng khác biệt (%d bảng):%n", struct.tableDiffs().size());

    for (var table : struct.tableDiffs()) {
      System.out.printf(
          "     + Bảng [%s] -> Loại khác biệt: %s%n", table.tableName(), table.diffType());

      if (!table.columnDiffs().isEmpty()) {
        System.out.println("       * Cột thay đổi:");
        for (var col : table.columnDiffs()) {
          System.out.printf("         - %s (%s):%n", col.columnName(), col.diffType());
          if (!col.sourceAttributes().isEmpty()) {
            System.out.printf("           Source: %s%n", col.sourceAttributes());
          }
          if (!col.targetAttributes().isEmpty()) {
            System.out.printf("           Target: %s%n", col.targetAttributes());
          }
        }
      }

      if (!table.addedIndexes().isEmpty()) {
        System.out.printf("       * Indexes thêm mới ở Target: %s%n", table.addedIndexes());
      }
      if (!table.removedIndexes().isEmpty()) {
        System.out.printf("       * Indexes bị gỡ bỏ ở Target: %s%n", table.removedIndexes());
      }
    }

    // --- 2. DỮ LIỆU (DATA DIFF) ---
    var data = diffResult.dataResult();
    System.out.println("\n2. DỮ LIỆU (DATA DIFF):");
    System.out.printf("   - Trạng thái: %s%n", data.isIdentical() ? "MATCHED" : "DIFFERENT");
    System.out.printf(
        "   - Chi tiết so sánh dữ liệu từng bảng (%d bảng):%n", data.tableDataDiffs().size());

    for (var tableData : data.tableDataDiffs()) {
      System.out.printf(
          "     + Bảng [%s] - Identical: %s%n", tableData.tableName(), tableData.isIdentical());
      if (!tableData.isIdentical()) {
        System.out.printf(
            "       * Số dòng thiếu ở Target (Cần INSERT): %d%n", tableData.missingInTargetCount());
        System.out.printf(
            "       * Số dòng thừa ở Target (Cần DELETE): %d%n", tableData.missingInSourceCount());
        System.out.printf(
            "       * Số dòng sai lệch dữ liệu (Cần UPDATE): %d%n", tableData.mismatchedRowCount());

        if (!tableData.sampleRowChanges().isEmpty()) {
          System.out.println("       * Mẫu các dòng bị lệch:");
          for (var change : tableData.sampleRowChanges()) {
            System.out.printf(
                "         - Type: %-6s | PrimaryKey: %s%n",
                change.changeType(), change.primaryKeys());
            if ("UPDATE".equalsIgnoreCase(change.changeType())
                && !change.fieldChanges().isEmpty()) {
              change
                  .fieldChanges()
                  .forEach(
                      (field, valPair) ->
                          System.out.printf(
                              "           > %s: Source = %s | Target = %s%n",
                              field, valPair.sourceValue(), valPair.targetValue()));
            }
          }
        }
      }
    }
  }
}
