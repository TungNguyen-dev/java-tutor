package tungnn.tutor.java.core.lib.jdbc.extractor;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Map;
import tungnn.tutor.java.core.lib.jdbc.extractor.dto.*;

public class Demo {

  static void main(String[] args) throws Exception {
    String url = System.getenv("DB_URL");
    String user = System.getenv("DB_USER");
    String password = System.getenv("DB_PASS");

    /*
     * Lưu ý với MySQL: Nếu muốn DatabaseMetaData trả về comment của Table/Column trong REMARKS, chuỗi JDBC URL của MySQL cần bổ sung parameter: jdbc:mysql://localhost:3306/db?useInformationSchema=true
     */

    if (url == null || user == null || password == null) {
      System.err.println(
          "Vui lòng cấu hình biến môi trường DB_URL, DB_USER, DB_PASS trước khi chạy!");
      return;
    }

    try (Connection conn = DriverManager.getConnection(url, user, password)) {
      DefaultSchemaExtractor extractorService = new DefaultSchemaExtractor();

      // Extractor tự động kích hoạt Strategy tương ứng (OracleSchemaStrategy /
      // GenericJdbcSchemaStrategy)
      SchemaMetadataDto schemaMetadata = extractorService.extractSchema(conn, null, "HR");

      printSchemaDetails(schemaMetadata);
    }
  }

  /** In toàn bộ chi tiết thông tin Schema -> Tables -> Columns / PKs / FKs / Indexes */
  private static void printSchemaDetails(SchemaMetadataDto schema) {
    System.out.println(
        "================================================================================");
    System.out.println(
        "                              DETAILED SCHEMA REPORT                            ");
    System.out.println(
        "================================================================================");
    System.out.printf("Catalog     : %s%n", schema.catalog() != null ? schema.catalog() : "N/A");
    System.out.printf("Schema Name : %s%n", schema.schemaName());
    System.out.printf("Total Tables: %d%n", schema.tables().size());
    System.out.println(
        "================================================================================");

    if (schema.tables().isEmpty()) {
      System.out.println(" Không tìm thấy table nào trong schema này.");
      return;
    }

    int tableIndex = 1;
    for (Map.Entry<String, TableMetadataDto> entry : schema.tables().entrySet()) {
      TableMetadataDto table = entry.getValue();

      System.out.println();
      System.out.printf(
          "[%d/%d] TABLE: %s%n",
          tableIndex++, schema.tables().size(), table.tableName().toUpperCase());
      System.out.printf("   ├── Type   : %s%n", table.tableType());
      System.out.printf(
          "   ├── Remarks: %s%n",
          table.remarks() != null ? table.remarks().replaceAll("\\R", "") : "N/A");

      // 1. In danh sách Columns
      printColumns(table.columns());

      // 2. In Primary Keys
      printPrimaryKeys(table.primaryKeys());

      // 3. In Foreign Keys
      printForeignKeys(table.foreignKeys());

      // 4. In Indexes
      printIndexes(table.indexes());

      System.out.println(
          "   -----------------------------------------------------------------------------");
    }
  }

  private static void printColumns(Map<String, ColumnDto> columns) {
    System.out.printf("   ├── Columns (%d):%n", columns.size());
    if (columns.isEmpty()) {
      System.out.println("   │     (Không có dữ liệu cột)");
      return;
    }

    System.out.printf(
        "   │     %-4s | %-25s | %-15s | %-6s | %-8s | %-8s | %-15s | %-25s%n",
        "#", "COLUMN NAME", "DATA TYPE", "SIZE", "NULLABLE", "AUTO INC", "DEFAULT", "REMARKS");
    System.out.println(
        "   │     -----+---------------------------+-----------------+--------+----------+----------+-----------------+--------------------------");

    for (ColumnDto col : columns.values()) {
      String remarks = col.remarks() != null ? col.remarks().trim() : "";

      // Rút gọn comment nếu dài quá 25 ký tự để tránh vỡ khung console
      if (remarks.length() > 25) {
        remarks = remarks.substring(0, 22) + "...";
      }

      System.out.printf(
          "   │     %-4d | %-25s | %-15s | %-6d | %-8b | %-8b | %-15s | %-25s%n",
          col.ordinalPosition(),
          col.columnName(),
          col.dataType(),
          col.columnSize(),
          col.isNullable(),
          col.isAutoIncrement(),
          col.defaultValue() != null ? col.defaultValue() : "NULL",
          remarks.isEmpty() ? "NULL" : remarks);
    }
  }

  private static void printPrimaryKeys(java.util.List<String> primaryKeys) {
    System.out.print("   ├── Primary Key(s): ");
    if (primaryKeys.isEmpty()) {
      System.out.println("[ NONE ]");
    } else {
      System.out.println(String.join(", ", primaryKeys));
    }
  }

  private static void printForeignKeys(java.util.List<ForeignKeyDto> foreignKeys) {
    System.out.printf("   ├── Foreign Keys (%d):%n", foreignKeys.size());
    if (foreignKeys.isEmpty()) {
      System.out.println("   │     [ NONE ]");
      return;
    }

    for (ForeignKeyDto fk : foreignKeys) {
      System.out.printf(
          "   │     • %s: %s -> %s(%s)%n",
          fk.fkName() != null ? fk.fkName() : "FK_UNNAMED",
          fk.fkColumnName(),
          fk.pkTableName(),
          fk.pkColumnName());
    }
  }

  private static void printIndexes(java.util.List<IndexDto> indexes) {
    System.out.printf("   └── Indexes (%d):%n", indexes.size());
    if (indexes.isEmpty()) {
      System.out.println("         [ NONE ]");
      return;
    }

    for (IndexDto idx : indexes) {
      String uniqueness = idx.isUnique() ? "UNIQUE" : "NON-UNIQUE";
      String columnsStr = String.join(", ", idx.columnNames());
      System.out.printf("         • %s [%s] -> (%s)%n", idx.indexName(), uniqueness, columnsStr);
    }
  }
}
