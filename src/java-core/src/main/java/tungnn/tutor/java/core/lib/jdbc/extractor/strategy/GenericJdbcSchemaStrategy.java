package tungnn.tutor.java.core.lib.jdbc.extractor.strategy;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import tungnn.tutor.java.core.lib.jdbc.extractor.dto.*;
import tungnn.tutor.java.core.lib.jdbc.extractor.utils.SchemaExtractorUtils;

public class GenericJdbcSchemaStrategy implements SchemaExtractorStrategy {

  @Override
  public boolean supports(Connection connection) throws SQLException {
    // Fallback strategy cho mọi loại DB
    return true;
  }

  @Override
  public SchemaMetadataDto extractSchema(Connection connection, String catalog, String schema)
      throws SQLException {
    DatabaseMetaData metaData = connection.getMetaData();

    // 1. Fetch Tables
    Map<String, TableBuilder> tableBuilders = fetchTables(metaData, catalog, schema);

    // 2. Fetch Columns
    fetchColumns(metaData, catalog, schema, tableBuilders);

    // 3. Fetch Primary Keys
    fetchPrimaryKeys(metaData, catalog, schema, tableBuilders);

    // 4. Fetch Foreign Keys
    fetchForeignKeys(metaData, catalog, schema, tableBuilders);

    // 5. Fetch Indexes
    fetchIndexes(metaData, catalog, schema, tableBuilders);

    // Build kết quả cuối cùng
    Map<String, TableMetadataDto> tablesMap = new HashMap<>();
    for (Map.Entry<String, TableBuilder> entry : tableBuilders.entrySet()) {
      tablesMap.put(entry.getKey(), entry.getValue().build());
    }

    return new SchemaMetadataDto(catalog, schema, tablesMap);
  }

  protected Map<String, TableBuilder> fetchTables(
      DatabaseMetaData metaData, String catalog, String schema) throws SQLException {
    Map<String, TableBuilder> tables = new HashMap<>();
    try (ResultSet rs = metaData.getTables(catalog, schema, "%", new String[] {"TABLE", "VIEW"})) {
      while (rs.next()) {
        String tableName = rs.getString("TABLE_NAME");
        if (tableName == null) continue;

        String normalizedName = SchemaExtractorUtils.normalize(tableName);
        String tableType = rs.getString("TABLE_TYPE");
        String remarks = rs.getString("REMARKS");

        tables.put(normalizedName, new TableBuilder(tableName, tableType, remarks));
      }
    }
    return tables;
  }

  protected void fetchColumns(
      DatabaseMetaData metaData,
      String catalog,
      String schema,
      Map<String, TableBuilder> tableBuilders)
      throws SQLException {
    try (ResultSet rs = metaData.getColumns(catalog, schema, "%", "%")) {
      while (rs.next()) {
        // Đọc theo Positional Index để tăng tính tương thích
        String tableName = SchemaExtractorUtils.normalize(rs.getString(3)); // TABLE_NAME
        TableBuilder builder = tableBuilders.get(tableName);
        if (builder == null) continue;

        String colName = rs.getString(4); // COLUMN_NAME
        String dataType = rs.getString(6); // TYPE_NAME
        int columnSize = rs.getInt(7); // COLUMN_SIZE
        String defaultValue = extractDefaultValueSafe(rs); // COLUMN_DEF (Index 13)
        boolean isNullable = "YES".equalsIgnoreCase(rs.getString(18)); // IS_NULLABLE
        boolean isAutoInc = extractAutoIncrementSafe(rs); // IS_AUTOINCREMENT (Index 23)
        int ordinalPos = rs.getInt(17); // ORDINAL_POSITION
        String columnRemarks = rs.getString(12); // REMARKS (Index 12) <-- Lấy comment ở đây

        ColumnDto column =
            new ColumnDto(
                colName,
                dataType,
                columnSize,
                isNullable,
                defaultValue,
                isAutoInc,
                ordinalPos,
                columnRemarks);
        builder.columns.put(SchemaExtractorUtils.normalize(colName), column);
      }
    }
  }

  protected void fetchPrimaryKeys(
      DatabaseMetaData metaData,
      String catalog,
      String schema,
      Map<String, TableBuilder> tableBuilders)
      throws SQLException {
    for (Map.Entry<String, TableBuilder> entry : tableBuilders.entrySet()) {
      TableBuilder builder = entry.getValue();
      try (ResultSet rs = metaData.getPrimaryKeys(catalog, schema, builder.rawTableName)) {
        List<String> pkList = new ArrayList<>();
        while (rs.next()) {
          pkList.add(rs.getString("COLUMN_NAME"));
        }
        builder.primaryKeys.addAll(pkList);
      } catch (SQLException ignored) {
        // Một số driver nổ ngoại lệ nếu table không có PK hoặc là VIEW
      }
    }
  }

  protected void fetchForeignKeys(
      DatabaseMetaData metaData,
      String catalog,
      String schema,
      Map<String, TableBuilder> tableBuilders)
      throws SQLException {
    for (Map.Entry<String, TableBuilder> entry : tableBuilders.entrySet()) {
      TableBuilder builder = entry.getValue();
      try (ResultSet rs = metaData.getImportedKeys(catalog, schema, builder.rawTableName)) {
        while (rs.next()) {
          String fkName = rs.getString("FK_NAME");
          String fkCol = rs.getString("FKCOLUMN_NAME");
          String pkTable = rs.getString("PKTABLE_NAME");
          String pkCol = rs.getString("PKCOLUMN_NAME");

          builder.foreignKeys.add(new ForeignKeyDto(fkName, fkCol, pkTable, pkCol));
        }
      } catch (SQLException ignored) {
      }
    }
  }

  protected void fetchIndexes(
      DatabaseMetaData metaData,
      String catalog,
      String schema,
      Map<String, TableBuilder> tableBuilders)
      throws SQLException {
    for (Map.Entry<String, TableBuilder> entry : tableBuilders.entrySet()) {
      TableBuilder builder = entry.getValue();
      try (ResultSet rs =
          metaData.getIndexInfo(catalog, schema, builder.rawTableName, false, false)) {
        Map<String, IndexBuilder> indexMap = new HashMap<>();

        while (rs.next()) {
          String indexName = rs.getString("INDEX_NAME");
          if (indexName == null) continue; // Phân biệt với Statistic Index

          boolean nonUnique = rs.getBoolean("NONUNIQUE");
          String columnName = rs.getString("COLUMN_NAME");

          indexMap
              .computeIfAbsent(indexName, k -> new IndexBuilder(indexName, !nonUnique))
              .columns
              .add(columnName);
        }

        for (IndexBuilder idxBuilder : indexMap.values()) {
          builder.indexes.add(idxBuilder.build());
        }
      } catch (SQLException ignored) {
      }
    }
  }

  private String extractDefaultValueSafe(ResultSet rs) {
    try {
      return rs.getString(13); // COLUMN_DEF
    } catch (SQLException e) {
      return null;
    }
  }

  private boolean extractAutoIncrementSafe(ResultSet rs) {
    try {
      return "YES".equalsIgnoreCase(rs.getString(23)); // IS_AUTOINCREMENT
    } catch (SQLException e) {
      return false;
    }
  }

  // Helper Builder class nội bộ dùng gom dữ liệu trong quá trình fetch
  protected static class TableBuilder {
    String rawTableName;
    String tableType;
    String remarks;
    Map<String, ColumnDto> columns = new LinkedHashMap<>();
    List<String> primaryKeys = new ArrayList<>();
    List<ForeignKeyDto> foreignKeys = new ArrayList<>();
    List<IndexDto> indexes = new ArrayList<>();

    TableBuilder(String rawTableName, String tableType, String remarks) {
      this.rawTableName = rawTableName;
      this.tableType = tableType;
      this.remarks = remarks;
    }

    TableMetadataDto build() {
      return new TableMetadataDto(
          rawTableName, tableType, remarks, columns, primaryKeys, foreignKeys, indexes);
    }
  }

  private static class IndexBuilder {
    String indexName;
    boolean isUnique;
    List<String> columns = new ArrayList<>();

    IndexBuilder(String indexName, boolean isUnique) {
      this.indexName = indexName;
      this.isUnique = isUnique;
    }

    IndexDto build() {
      return new IndexDto(indexName, isUnique, columns);
    }
  }
}
