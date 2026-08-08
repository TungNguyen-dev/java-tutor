package tungnn.tutor.java.core.lib.jdbc.extractor.strategy;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import tungnn.tutor.java.core.lib.jdbc.extractor.dto.*;
import tungnn.tutor.java.core.lib.jdbc.extractor.utils.SchemaExtractorUtils;

public class OracleSchemaStrategy extends GenericJdbcSchemaStrategy {

  @Override
  public boolean supports(Connection connection) throws SQLException {
    String dbProduct = connection.getMetaData().getDatabaseProductName();
    return dbProduct != null && dbProduct.toLowerCase(Locale.ROOT).contains("oracle");
  }

  @Override
  public SchemaMetadataDto extractSchema(Connection connection, String catalog, String schema)
      throws SQLException {
    String targetSchema = (schema != null && !schema.isBlank()) ? schema : connection.getSchema();
    if (targetSchema == null) {
      targetSchema = connection.getMetaData().getUserName();
    }
    targetSchema = targetSchema.toUpperCase(Locale.ROOT);

    Map<String, TableBuilder> tableBuilders = fetchOracleTables(connection, targetSchema);
    fetchOracleColumns(connection, targetSchema, tableBuilders);
    fetchOracleConstraintsAndPKFK(connection, targetSchema, tableBuilders);
    fetchOracleIndexes(connection, targetSchema, tableBuilders);

    Map<String, TableMetadataDto> tablesMap = new HashMap<>();
    for (Map.Entry<String, TableBuilder> entry : tableBuilders.entrySet()) {
      tablesMap.put(entry.getKey(), entry.getValue().build());
    }

    return new SchemaMetadataDto(catalog, targetSchema, tablesMap);
  }

  private Map<String, TableBuilder> fetchOracleTables(Connection conn, String schema)
      throws SQLException {
    // ALL_TAB_COMMENTS có sẵn TABLE_NAME, TABLE_TYPE, và COMMENTS
    String sql =
        "SELECT TABLE_NAME, TABLE_TYPE, COMMENTS "
            + "FROM ALL_TAB_COMMENTS "
            + "WHERE OWNER = ? AND TABLE_TYPE IN ('TABLE', 'VIEW')";

    Map<String, TableBuilder> tables = new HashMap<>();

    try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
      pstmt.setString(1, schema);
      try (ResultSet rs = pstmt.executeQuery()) {
        while (rs.next()) {
          String tableName = rs.getString("TABLE_NAME");
          String tableType = rs.getString("TABLE_TYPE");
          String remarks = rs.getString("COMMENTS");

          tables.put(
              SchemaExtractorUtils.normalize(tableName),
              new TableBuilder(tableName, tableType != null ? tableType : "TABLE", remarks));
        }
      }
    }
    return tables;
  }

  private void fetchOracleColumns(Connection conn, String schema, Map<String, TableBuilder> tables)
      throws SQLException {
    // JOIN thêm ALL_COL_COMMENTS
    String sql =
        "SELECT c.TABLE_NAME, c.COLUMN_NAME, c.DATA_TYPE, c.DATA_LENGTH, c.NULLABLE, "
            + "       c.DATA_DEFAULT, c.IDENTITY_COLUMN, c.COLUMN_ID, cm.COMMENTS "
            + "FROM ALL_TAB_COLS c "
            + "LEFT JOIN ALL_COL_COMMENTS cm ON c.OWNER = cm.OWNER AND c.TABLE_NAME = cm.TABLE_NAME AND c.COLUMN_NAME = cm.COLUMN_NAME "
            + "WHERE c.OWNER = ? AND c.HIDDEN_COLUMN = 'NO' "
            + "ORDER BY c.TABLE_NAME, c.COLUMN_ID";

    try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
      pstmt.setString(1, schema);
      try (ResultSet rs = pstmt.executeQuery()) {
        while (rs.next()) {
          String tableName = SchemaExtractorUtils.normalize(rs.getString("TABLE_NAME"));
          TableBuilder builder = tables.get(tableName);
          if (builder == null) continue;

          String colName = rs.getString("COLUMN_NAME");
          String dataType = rs.getString("DATA_TYPE");
          int columnSize = rs.getInt("DATA_LENGTH");
          boolean isNullable = "Y".equalsIgnoreCase(rs.getString("NULLABLE"));
          String defaultValue = rs.getString("DATA_DEFAULT");
          boolean isAutoInc = "YES".equalsIgnoreCase(rs.getString("IDENTITY_COLUMN"));
          int ordinalPos = rs.getInt("COLUMN_ID");
          String columnRemarks = rs.getString("COMMENTS"); // <-- Lấy comment từ ALL_COL_COMMENTS

          ColumnDto column =
              new ColumnDto(
                  colName,
                  dataType,
                  columnSize,
                  isNullable,
                  defaultValue != null ? defaultValue.trim() : null,
                  isAutoInc,
                  ordinalPos,
                  columnRemarks);
          builder.columns.put(SchemaExtractorUtils.normalize(colName), column);
        }
      }
    }
  }

  private void fetchOracleConstraintsAndPKFK(
      Connection conn, String schema, Map<String, TableBuilder> tables) throws SQLException {
    String sql =
        "SELECT c.TABLE_NAME, c.CONSTRAINT_NAME, c.CONSTRAINT_TYPE, cc.COLUMN_NAME, "
            + "       r.TABLE_NAME AS PK_TABLE_NAME, rc.COLUMN_NAME AS PK_COLUMN_NAME "
            + "FROM ALL_CONSTRAINTS c "
            + "JOIN ALL_CONS_COLUMNS cc ON c.OWNER = cc.OWNER AND c.CONSTRAINT_NAME = cc.CONSTRAINT_NAME "
            + "LEFT JOIN ALL_CONSTRAINTS r ON c.R_OWNER = r.OWNER AND c.R_CONSTRAINT_NAME = r.CONSTRAINT_NAME "
            + "LEFT JOIN ALL_CONS_COLUMNS rc ON r.OWNER = rc.OWNER AND r.CONSTRAINT_NAME = rc.CONSTRAINT_NAME AND cc.POSITION = rc.POSITION "
            + "WHERE c.OWNER = ? AND c.CONSTRAINT_TYPE IN ('P', 'R') "
            + "ORDER BY c.TABLE_NAME, c.CONSTRAINT_NAME, cc.POSITION";

    try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
      pstmt.setString(1, schema);
      try (ResultSet rs = pstmt.executeQuery()) {
        while (rs.next()) {
          String tableName = SchemaExtractorUtils.normalize(rs.getString("TABLE_NAME"));
          TableBuilder builder = tables.get(tableName);
          if (builder == null) continue;

          String type = rs.getString("CONSTRAINT_TYPE");
          String colName = rs.getString("COLUMN_NAME");

          if ("P".equals(type)) {
            builder.primaryKeys.add(colName);
          } else if ("R".equals(type)) {
            String fkName = rs.getString("CONSTRAINT_NAME");
            String pkTable = rs.getString("PK_TABLE_NAME");
            String pkCol = rs.getString("PK_COLUMN_NAME");
            builder.foreignKeys.add(new ForeignKeyDto(fkName, colName, pkTable, pkCol));
          }
        }
      }
    }
  }

  private void fetchOracleIndexes(Connection conn, String schema, Map<String, TableBuilder> tables)
      throws SQLException {
    String sql =
        "SELECT i.TABLE_NAME, i.INDEX_NAME, i.UNIQUENESS, ic.COLUMN_NAME "
            + "FROM ALL_INDEXES i "
            + "JOIN ALL_IND_COLUMNS ic ON i.OWNER = ic.INDEX_OWNER AND i.INDEX_NAME = ic.INDEX_NAME "
            + "WHERE i.OWNER = ? "
            + "ORDER BY i.TABLE_NAME, i.INDEX_NAME, ic.COLUMN_POSITION";

    try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
      pstmt.setString(1, schema);
      try (ResultSet rs = pstmt.executeQuery()) {
        Map<String, Map<String, List<String>>> indexColsByTable = new HashMap<>();
        Map<String, Map<String, Boolean>> indexUniquenessByTable = new HashMap<>();

        while (rs.next()) {
          String tableName = SchemaExtractorUtils.normalize(rs.getString("TABLE_NAME"));
          String indexName = rs.getString("INDEX_NAME");
          boolean isUnique = "UNIQUE".equalsIgnoreCase(rs.getString("UNIQUENESS"));
          String colName = rs.getString("COLUMN_NAME");

          indexColsByTable
              .computeIfAbsent(tableName, k -> new HashMap<>())
              .computeIfAbsent(indexName, k -> new ArrayList<>())
              .add(colName);

          indexUniquenessByTable
              .computeIfAbsent(tableName, k -> new HashMap<>())
              .put(indexName, isUnique);
        }

        for (Map.Entry<String, Map<String, List<String>>> tableEntry :
            indexColsByTable.entrySet()) {
          TableBuilder builder = tables.get(tableEntry.getKey());
          if (builder == null) continue;

          for (Map.Entry<String, List<String>> idxEntry : tableEntry.getValue().entrySet()) {
            String idxName = idxEntry.getKey();
            boolean isUnique = indexUniquenessByTable.get(tableEntry.getKey()).get(idxName);
            builder.indexes.add(new IndexDto(idxName, isUnique, idxEntry.getValue()));
          }
        }
      }
    }
  }
}
