package tungnn.tutor.java.core.lib.jdbc.extractor.strategy;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import tungnn.tutor.java.core.lib.jdbc.extractor.dto.*;
import tungnn.tutor.java.core.lib.jdbc.extractor.utils.SchemaExtractorUtils;

public class PostgresSchemaStrategy extends GenericJdbcSchemaStrategy {

  @Override
  public boolean supports(Connection connection) throws SQLException {
    String dbProduct = connection.getMetaData().getDatabaseProductName();
    return dbProduct != null && dbProduct.toLowerCase(Locale.ROOT).contains("postgresql");
  }

  @Override
  public SchemaMetadataDto extractSchema(Connection connection, String catalog, String schema)
      throws SQLException {

    // PostgreSQL: Catalog là Database Name, Schema thường là "public" nếu null
    String targetCatalog =
        (catalog != null && !catalog.isBlank()) ? catalog : connection.getCatalog();

    String targetSchema = (schema != null && !schema.isBlank()) ? schema : connection.getSchema();

    if (targetSchema == null) {
      targetSchema = "public";
    }

    Map<String, TableBuilder> tableBuilders = fetchPostgresTables(connection, targetSchema);
    fetchPostgresColumns(connection, targetSchema, tableBuilders);
    fetchPostgresConstraintsAndPKFK(connection, targetSchema, tableBuilders);
    fetchPostgresIndexes(connection, targetSchema, tableBuilders);

    Map<String, TableMetadataDto> tablesMap = new HashMap<>();
    for (Map.Entry<String, TableBuilder> entry : tableBuilders.entrySet()) {
      tablesMap.put(entry.getKey(), entry.getValue().build());
    }

    return new SchemaMetadataDto(targetCatalog, targetSchema, tablesMap);
  }

  /** 1. Extract Tables, Views & Table Comments qua information_schema & pg_description */
  private Map<String, TableBuilder> fetchPostgresTables(Connection conn, String schema)
      throws SQLException {
    String sql =
        "SELECT "
            + "    t.table_name, "
            + "    t.table_type, "
            + "    pg_catalog.obj_description(c.oid, 'pg_class') AS remarks "
            + "FROM information_schema.tables t "
            + "JOIN pg_catalog.pg_class c ON c.relname = t.table_name "
            + "JOIN pg_catalog.pg_namespace n ON n.oid = c.relnamespace AND n.nspname = t.table_schema "
            + "WHERE t.table_schema = ? "
            + "  AND t.table_type IN ('BASE TABLE', 'VIEW')";

    Map<String, TableBuilder> tables = new HashMap<>();

    try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
      pstmt.setString(1, schema);
      try (ResultSet rs = pstmt.executeQuery()) {
        while (rs.next()) {
          String tableName = rs.getString("table_name");
          String rawTableType = rs.getString("table_type");
          String remarks = rs.getString("remarks");

          // Standardize Table Type: BASE TABLE -> TABLE
          String tableType = "BASE TABLE".equalsIgnoreCase(rawTableType) ? "TABLE" : rawTableType;

          tables.put(
              SchemaExtractorUtils.normalize(tableName),
              new TableBuilder(tableName, tableType, remarks));
        }
      }
    }
    return tables;
  }

  /** 2. Extract Columns, Data Types, Column Defaults, AutoIncrement & Column Comments */
  private void fetchPostgresColumns(
      Connection conn, String schema, Map<String, TableBuilder> tables) throws SQLException {
    String sql =
        "SELECT "
            + "    cols.table_name, "
            + "    cols.column_name, "
            + "    cols.udt_name AS data_type, "
            + "    COALESCE(cols.character_maximum_length, cols.numeric_precision, 0) AS column_size, "
            + "    cols.is_nullable, "
            + "    cols.column_default, "
            + "    cols.is_identity, "
            + "    cols.ordinal_position, "
            + "    pg_catalog.col_description(c.oid, cols.ordinal_position::int) AS remarks "
            + "FROM information_schema.columns cols "
            + "JOIN pg_catalog.pg_class c ON c.relname = cols.table_name "
            + "JOIN pg_catalog.pg_namespace n ON n.oid = c.relnamespace AND n.nspname = cols.table_schema "
            + "WHERE cols.table_schema = ? "
            + "ORDER BY cols.table_name, cols.ordinal_position";

    try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
      pstmt.setString(1, schema);
      try (ResultSet rs = pstmt.executeQuery()) {
        while (rs.next()) {
          String tableName = SchemaExtractorUtils.normalize(rs.getString("table_name"));
          TableBuilder builder = tables.get(tableName);
          if (builder == null) continue;

          String colName = rs.getString("column_name");
          String dataType = rs.getString("data_type");
          int columnSize = rs.getInt("column_size");
          boolean isNullable = "YES".equalsIgnoreCase(rs.getString("is_nullable"));
          String defaultValue = rs.getString("column_default");
          String isIdentity = rs.getString("is_identity");
          int ordinalPos = rs.getInt("ordinal_position");
          String remarks = rs.getString("remarks");

          // Check Auto Increment: Serial/BigSerial (dùng sequence default) hoặc Postgres 10+
          // Identity Column
          boolean isAutoInc =
              "YES".equalsIgnoreCase(isIdentity)
                  || (defaultValue != null
                      && defaultValue.toLowerCase(Locale.ROOT).contains("nextval("));

          ColumnDto column =
              new ColumnDto(
                  colName,
                  dataType,
                  columnSize,
                  isNullable,
                  defaultValue != null ? defaultValue.trim() : null,
                  isAutoInc,
                  ordinalPos,
                  remarks);

          builder.columns.put(SchemaExtractorUtils.normalize(colName), column);
        }
      }
    }
  }

  /** 3. Extract Primary Keys & Foreign Keys qua information_schema */
  private void fetchPostgresConstraintsAndPKFK(
      Connection conn, String schema, Map<String, TableBuilder> tables) throws SQLException {
    String sql =
        "SELECT "
            + "    tc.table_name, "
            + "    tc.constraint_name, "
            + "    tc.constraint_type, "
            + "    kcu.column_name, "
            + "    ccu.table_name AS pk_table_name, "
            + "    ccu.column_name AS pk_column_name "
            + "FROM information_schema.table_constraints tc "
            + "JOIN information_schema.key_column_usage kcu "
            + "  ON tc.constraint_name = kcu.constraint_name AND tc.table_schema = kcu.table_schema "
            + "LEFT JOIN information_schema.constraint_column_usage ccu "
            + "  ON tc.constraint_name = ccu.constraint_name AND tc.table_schema = ccu.table_schema "
            + "WHERE tc.table_schema = ? "
            + "  AND tc.constraint_type IN ('PRIMARY KEY', 'FOREIGN KEY') "
            + "ORDER BY tc.table_name, tc.constraint_name, kcu.ordinal_position";

    try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
      pstmt.setString(1, schema);
      try (ResultSet rs = pstmt.executeQuery()) {
        while (rs.next()) {
          String tableName = SchemaExtractorUtils.normalize(rs.getString("table_name"));
          TableBuilder builder = tables.get(tableName);
          if (builder == null) continue;

          String type = rs.getString("constraint_type");
          String colName = rs.getString("column_name");

          if ("PRIMARY KEY".equalsIgnoreCase(type)) {
            builder.primaryKeys.add(colName);
          } else if ("FOREIGN KEY".equalsIgnoreCase(type)) {
            String fkName = rs.getString("constraint_name");
            String pkTable = rs.getString("pk_table_name");
            String pkCol = rs.getString("pk_column_name");

            builder.foreignKeys.add(new ForeignKeyDto(fkName, colName, pkTable, pkCol));
          }
        }
      }
    }
  }

  /** 4. Extract Indexes & Uniqueness qua pg_catalog */
  private void fetchPostgresIndexes(
      Connection conn, String schema, Map<String, TableBuilder> tables) throws SQLException {
    String sql =
        "SELECT "
            + "    t.relname AS table_name, "
            + "    i.relname AS index_name, "
            + "    idx.indisunique AS is_unique, "
            + "    a.attname AS column_name "
            + "FROM pg_class t "
            + "JOIN pg_namespace n ON n.oid = t.relnamespace "
            + "JOIN pg_index idx ON t.oid = idx.indrelid "
            + "JOIN pg_class i ON i.oid = idx.indexrelid "
            + "JOIN pg_attribute a ON a.attrelid = t.oid AND a.attnum = ANY(idx.indkey) "
            + "WHERE n.nspname = ? "
            + "  AND t.relkind IN ('r', 'm') " // 'r' = ordinary table, 'm' = materialized view
            + "ORDER BY t.relname, i.relname, a.attnum";

    try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
      pstmt.setString(1, schema);
      try (ResultSet rs = pstmt.executeQuery()) {
        Map<String, Map<String, List<String>>> indexColsByTable = new HashMap<>();
        Map<String, Map<String, Boolean>> indexUniquenessByTable = new HashMap<>();

        while (rs.next()) {
          String tableName = SchemaExtractorUtils.normalize(rs.getString("table_name"));
          String indexName = rs.getString("index_name");
          boolean isUnique = rs.getBoolean("is_unique");
          String colName = rs.getString("column_name");

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
