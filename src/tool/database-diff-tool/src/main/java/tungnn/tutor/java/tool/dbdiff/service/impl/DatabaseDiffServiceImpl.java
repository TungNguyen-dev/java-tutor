package tungnn.tutor.java.tool.dbdiff.service.impl;

import java.sql.*;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;
import tungnn.tutor.java.core.lib.jdbc.extractor.SchemaExtractor;
import tungnn.tutor.java.core.lib.jdbc.extractor.dto.ColumnDto;
import tungnn.tutor.java.core.lib.jdbc.extractor.dto.IndexDto;
import tungnn.tutor.java.core.lib.jdbc.extractor.dto.SchemaMetadataDto;
import tungnn.tutor.java.core.lib.jdbc.extractor.dto.TableMetadataDto;
import tungnn.tutor.java.tool.dbdiff.model.DiffModels;
import tungnn.tutor.java.tool.dbdiff.service.DatabaseDiffService;

public class DatabaseDiffServiceImpl implements DatabaseDiffService {

  private final SchemaExtractor schemaExtractor;

  public DatabaseDiffServiceImpl(SchemaExtractor schemaExtractor) {
    this.schemaExtractor = schemaExtractor;
  }

  @Override
  public DiffModels.DiffSchemaStructureResult diffSchemaStructure(
      DiffModels.DatabaseConfig source, DiffModels.DatabaseConfig target) {
    return diffSchemaStructure(source, target, DiffModels.DiffOptions.defaults());
  }

  @Override
  public DiffModels.DiffSchemaStructureResult diffSchemaStructure(
      DiffModels.DatabaseConfig source,
      DiffModels.DatabaseConfig target,
      DiffModels.DiffOptions options) {

    DiffModels.DiffOptions effectiveOptions =
        options != null ? options : DiffModels.DiffOptions.defaults();

    SchemaMetadataDto sourceSchema = extractSchemaMetadata(source);
    SchemaMetadataDto targetSchema = extractSchemaMetadata(target);

    Set<String> allTableNames = new TreeSet<>(String.CASE_INSENSITIVE_ORDER);
    allTableNames.addAll(sourceSchema.tables().keySet());
    allTableNames.addAll(targetSchema.tables().keySet());

    List<DiffModels.TableStructureDiff> tableDiffs = new ArrayList<>();
    boolean isOverallIdentical = true;

    for (String tableName : allTableNames) {
      if (shouldNotProcessTable(tableName, effectiveOptions)) {
        continue;
      }

      TableMetadataDto sourceTable = sourceSchema.tables().get(tableName);
      TableMetadataDto targetTable = targetSchema.tables().get(tableName);

      DiffModels.TableStructureDiff tableDiff =
          diffTableStructure(tableName, sourceTable, targetTable);
      if (!"MATCHED".equalsIgnoreCase(tableDiff.diffType())) {
        isOverallIdentical = false;
      }
      tableDiffs.add(tableDiff);
    }

    return new DiffModels.DiffSchemaStructureResult(
        Instant.now(), isOverallIdentical, List.copyOf(tableDiffs));
  }

  @Override
  public DiffModels.DiffSchemaDataResult diffSchemaData(
      DiffModels.DatabaseConfig source, DiffModels.DatabaseConfig target) {

    return diffSchemaData(source, target, DiffModels.DiffOptions.defaults());
  }

  @Override
  public DiffModels.DiffSchemaDataResult diffSchemaData(
      DiffModels.DatabaseConfig source,
      DiffModels.DatabaseConfig target,
      DiffModels.DiffOptions options) {
    DiffModels.DiffOptions effectiveOptions =
        options != null ? options : DiffModels.DiffOptions.defaults();

    SchemaMetadataDto sourceSchema = extractSchemaMetadata(source);
    SchemaMetadataDto targetSchema = extractSchemaMetadata(target);

    // Lọc danh sách bảng hợp lệ theo DiffOptions
    Set<String> sourceTables =
        sourceSchema.tables().keySet().stream()
            .filter(t -> !shouldNotProcessTable(t, effectiveOptions))
            .collect(Collectors.toCollection(() -> new TreeSet<>(String.CASE_INSENSITIVE_ORDER)));

    Set<String> targetTables =
        targetSchema.tables().keySet().stream()
            .filter(t -> !shouldNotProcessTable(t, effectiveOptions))
            .collect(Collectors.toCollection(() -> new TreeSet<>(String.CASE_INSENSITIVE_ORDER)));

    // Lấy giao tập các bảng xuất hiện ở cả Source và Target
    Set<String> commonTables = new TreeSet<>(String.CASE_INSENSITIVE_ORDER);
    commonTables.addAll(sourceTables);
    commonTables.retainAll(targetTables);

    // --- LOGIC MỚI: XỬ LÝ TRƯỜNG HỢP KHÔNG CÓ BẢNG CHUNG ---
    if (commonTables.isEmpty()) {
      // Nếu 1 trong 2 schema có chứa bảng nhưng không giao nhau -> Báo DIFFERENT (isIdentical =
      // false)
      // Nếu cả 2 schema đều thực sự không có bảng nào -> Báo IDENTICAL (isIdentical = true)
      boolean isBothEmpty = sourceTables.isEmpty() && targetTables.isEmpty();
      return new DiffModels.DiffSchemaDataResult(Instant.now(), isBothEmpty, List.of());
    }

    List<DiffModels.TableDataDiff> tableDataDiffs = new ArrayList<>();
    boolean isOverallIdentical = true;

    try (Connection sourceConn = createConnection(source);
        Connection targetConn = createConnection(target)) {

      for (String tableName : commonTables) {
        TableMetadataDto sourceTable = sourceSchema.tables().get(tableName);
        TableMetadataDto targetTable = targetSchema.tables().get(tableName);

        DiffModels.TableDataDiff dataDiff =
            diffTableData(
                sourceConn,
                targetConn,
                source.schemaName(),
                target.schemaName(),
                sourceTable,
                targetTable,
                effectiveOptions.maxDataDiffLimitCount());

        if (!dataDiff.isIdentical()) {
          isOverallIdentical = false;
        }
        tableDataDiffs.add(dataDiff);
      }

    } catch (SQLException e) {
      throw new RuntimeException("Lỗi kết nối CSDL khi so sánh dữ liệu: " + e.getMessage(), e);
    }

    return new DiffModels.DiffSchemaDataResult(
        Instant.now(), isOverallIdentical, List.copyOf(tableDataDiffs));
  }

  // =========================================================================
  // HELPER METHODS: STRUCTURE DIFF
  // =========================================================================

  private DiffModels.TableStructureDiff diffTableStructure(
      String tableName, TableMetadataDto sourceTable, TableMetadataDto targetTable) {

    if (sourceTable == null) {
      return new DiffModels.TableStructureDiff(tableName, "ADDED", List.of(), List.of(), List.of());
    }
    if (targetTable == null) {
      return new DiffModels.TableStructureDiff(
          tableName, "REMOVED", List.of(), List.of(), List.of());
    }

    // 1. Column Diffs
    List<DiffModels.ColumnDiff> columnDiffs = new ArrayList<>();
    Set<String> allColumns = new TreeSet<>(String.CASE_INSENSITIVE_ORDER);
    allColumns.addAll(sourceTable.columns().keySet());
    allColumns.addAll(targetTable.columns().keySet());

    for (String colName : allColumns) {
      ColumnDto sourceCol = sourceTable.columns().get(colName);
      ColumnDto targetCol = targetTable.columns().get(colName);

      if (sourceCol == null) {
        columnDiffs.add(
            new DiffModels.ColumnDiff(
                colName, "ADDED", Map.of(), extractColumnAttributes(targetCol)));
      } else if (targetCol == null) {
        columnDiffs.add(
            new DiffModels.ColumnDiff(
                colName, "REMOVED", extractColumnAttributes(sourceCol), Map.of()));
      } else {
        Map<String, String> srcAttr = extractColumnAttributes(sourceCol);
        Map<String, String> tgtAttr = extractColumnAttributes(targetCol);

        if (!srcAttr.equals(tgtAttr)) {
          columnDiffs.add(new DiffModels.ColumnDiff(colName, "MODIFIED", srcAttr, tgtAttr));
        }
      }
    }

    // 2. Index Diffs
    Map<String, IndexDto> sourceIndexes = indexMapByName(sourceTable.indexes());
    Map<String, IndexDto> targetIndexes = indexMapByName(targetTable.indexes());

    List<String> addedIndexes = new ArrayList<>();
    List<String> removedIndexes = new ArrayList<>();

    for (String idxName : targetIndexes.keySet()) {
      if (!sourceIndexes.containsKey(idxName)) {
        addedIndexes.add(idxName);
      }
    }
    for (String idxName : sourceIndexes.keySet()) {
      if (!targetIndexes.containsKey(idxName)) {
        removedIndexes.add(idxName);
      }
    }

    String diffType =
        (columnDiffs.isEmpty() && addedIndexes.isEmpty() && removedIndexes.isEmpty())
            ? "MATCHED"
            : "MODIFIED";

    return new DiffModels.TableStructureDiff(
        tableName,
        diffType,
        List.copyOf(columnDiffs),
        List.copyOf(addedIndexes),
        List.copyOf(removedIndexes));
  }

  private Map<String, String> extractColumnAttributes(ColumnDto col) {
    Map<String, String> attrs = new LinkedHashMap<>();
    attrs.put("dataType", col.dataType() != null ? col.dataType() : "");
    attrs.put("columnSize", String.valueOf(col.columnSize()));
    attrs.put("isNullable", String.valueOf(col.isNullable()));
    attrs.put("defaultValue", col.defaultValue() != null ? col.defaultValue() : "");
    attrs.put("isAutoIncrement", String.valueOf(col.isAutoIncrement()));
    return attrs;
  }

  private Map<String, IndexDto> indexMapByName(List<IndexDto> indexes) {
    if (indexes == null) return Map.of();
    return indexes.stream()
        .filter(idx -> idx.indexName() != null)
        .collect(
            Collectors.toMap(IndexDto::indexName, idx -> idx, (existing, replacement) -> existing));
  }

  // =========================================================================
  // HELPER METHODS: DATA DIFF (TWO-POINTER STREAMING)
  // =========================================================================

  private DiffModels.TableDataDiff diffTableData(
      Connection sourceConn,
      Connection targetConn,
      String sourceSchema,
      String targetSchema,
      TableMetadataDto sourceTable,
      TableMetadataDto targetTable,
      int maxDiffLimit) {

    List<String> primaryKeys = sourceTable.primaryKeys();
    if (primaryKeys.isEmpty()) {
      // Nếu không có Primary Key, không thể thực hiện hai con trỏ Sort-Merge Data Diff chính xác
      return new DiffModels.TableDataDiff(sourceTable.tableName(), true, 0, 0, 0, List.of());
    }

    // Các cột chung giữa 2 bảng
    List<String> commonColumns = new ArrayList<>(sourceTable.columns().keySet());
    commonColumns.retainAll(targetTable.columns().keySet());

    String orderByClause = String.join(", ", primaryKeys);

    // Dynamic schema name cho từng connection
    String sourceTableFullName = buildQualifiedTableName(sourceSchema, sourceTable.tableName());
    String targetTableFullName = buildQualifiedTableName(targetSchema, targetTable.tableName());

    String sourceQuery =
        "SELECT * FROM %s ORDER BY %s".formatted(sourceTableFullName, orderByClause);
    String targetQuery =
        "SELECT * FROM %s ORDER BY %s".formatted(targetTableFullName, orderByClause);

    long missingInTarget = 0;
    long missingInSource = 0;
    long mismatchedCount = 0;
    List<DiffModels.RowChange> samples = new ArrayList<>();

    try (Statement stmtSrc = createStreamingStatement(sourceConn);
        Statement stmtTgt = createStreamingStatement(targetConn);
        ResultSet rsSrc = stmtSrc.executeQuery(sourceQuery);
        ResultSet rsTgt = stmtTgt.executeQuery(targetQuery)) {

      boolean hasSrc = rsSrc.next();
      boolean hasTgt = rsTgt.next();

      while (hasSrc || hasTgt) {
        if (hasSrc && hasTgt) {
          int cmp = comparePrimaryKeys(rsSrc, rsTgt, primaryKeys);
          if (cmp == 0) {
            // Cùng PK: Kiểm tra dữ liệu từng cột xem có mismatch không
            Map<String, DiffModels.ValuePair> fieldChanges =
                extractFieldChanges(rsSrc, rsTgt, commonColumns);

            if (!fieldChanges.isEmpty()) {
              mismatchedCount++;
              if (samples.size() < maxDiffLimit) {
                samples.add(
                    new DiffModels.RowChange(
                        "UPDATE", extractPKValues(rsSrc, primaryKeys), fieldChanges));
              }
            }
            hasSrc = rsSrc.next();
            hasTgt = rsTgt.next();
          } else if (cmp < 0) {
            missingInTarget++;
            if (samples.size() < maxDiffLimit) {
              samples.add(
                  new DiffModels.RowChange(
                      "DELETE", extractPKValues(rsSrc, primaryKeys), Map.of()));
            }
            hasSrc = rsSrc.next();
          } else {
            missingInSource++;
            if (samples.size() < maxDiffLimit) {
              samples.add(
                  new DiffModels.RowChange(
                      "INSERT", extractPKValues(rsTgt, primaryKeys), Map.of()));
            }
            hasTgt = rsTgt.next();
          }
        } else if (hasSrc) {
          missingInTarget++;
          if (samples.size() < maxDiffLimit) {
            samples.add(
                new DiffModels.RowChange("DELETE", extractPKValues(rsSrc, primaryKeys), Map.of()));
          }
          hasSrc = rsSrc.next();
        } else {
          missingInSource++;
          if (samples.size() < maxDiffLimit) {
            samples.add(
                new DiffModels.RowChange("INSERT", extractPKValues(rsTgt, primaryKeys), Map.of()));
          }
          hasTgt = rsTgt.next();
        }
      }

    } catch (SQLException e) {
      throw new RuntimeException(
          "Lỗi thực thi truy vấn so sánh dữ liệu bảng "
              + sourceTable.tableName()
              + ": "
              + e.getMessage(),
          e);
    }

    boolean isIdentical = (missingInTarget == 0 && missingInSource == 0 && mismatchedCount == 0);

    return new DiffModels.TableDataDiff(
        sourceTable.tableName(),
        isIdentical,
        missingInTarget,
        missingInSource,
        mismatchedCount,
        List.copyOf(samples));
  }

  @SuppressWarnings({"unchecked", "rawtypes"})
  private int comparePrimaryKeys(ResultSet rs1, ResultSet rs2, List<String> primaryKeys)
      throws SQLException {
    for (String pk : primaryKeys) {
      Object val1 = rs1.getObject(pk);
      Object val2 = rs2.getObject(pk);

      if (val1 == null && val2 == null) continue;
      if (val1 == null) return -1;
      if (val2 == null) return 1;

      if (val1 instanceof Comparable comp1 && val2 instanceof Comparable comp2) {
        int res = comp1.compareTo(comp2);
        if (res != 0) return res;
      } else {
        int res = val1.toString().compareTo(val2.toString());
        if (res != 0) return res;
      }
    }
    return 0;
  }

  private Map<String, Object> extractPKValues(ResultSet rs, List<String> primaryKeys)
      throws SQLException {
    Map<String, Object> pkMap = new LinkedHashMap<>();
    for (String pk : primaryKeys) {
      pkMap.put(pk, rs.getObject(pk));
    }
    return pkMap;
  }

  private Map<String, DiffModels.ValuePair> extractFieldChanges(
      ResultSet rsSrc, ResultSet rsTgt, List<String> columns) throws SQLException {
    Map<String, DiffModels.ValuePair> changes = new HashMap<>();
    for (String col : columns) {
      Object valSrc = rsSrc.getObject(col);
      Object valTgt = rsTgt.getObject(col);

      if (!Objects.equals(valSrc, valTgt)) {
        changes.put(col, new DiffModels.ValuePair(valSrc, valTgt));
      }
    }
    return changes;
  }

  // =========================================================================
  // UTILS
  // =========================================================================

  private SchemaMetadataDto extractSchemaMetadata(DiffModels.DatabaseConfig config) {
    try (Connection conn = createConnection(config)) {
      return schemaExtractor.extractSchema(conn, null, config.schemaName());
    } catch (SQLException e) {
      throw new RuntimeException("Không thể lấy Metadata cho schema: " + config.schemaName(), e);
    }
  }

  private Connection createConnection(DiffModels.DatabaseConfig config) throws SQLException {
    return DriverManager.getConnection(config.jdbcUrl(), config.username(), config.password());
  }

  private Statement createStreamingStatement(Connection conn) throws SQLException {
    Statement stmt = conn.createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
    // Fetch size tối ưu hóa bộ nhớ cho MySQL / PostgreSQL / Oracle
    stmt.setFetchSize(1000);
    return stmt;
  }

  private boolean shouldNotProcessTable(String tableName, DiffModels.DiffOptions options) {
    if (options.excludeTables() != null && !options.excludeTables().isEmpty()) {
      if (containsIgnoreCase(options.excludeTables(), tableName)) {
        return true;
      }
    }
    if (options.includeTables() != null && !options.includeTables().isEmpty()) {
      return !containsIgnoreCase(options.includeTables(), tableName);
    }
    return false;
  }

  private boolean containsIgnoreCase(List<String> list, String value) {
    return list.stream().anyMatch(item -> item.equalsIgnoreCase(value));
  }

  private String buildQualifiedTableName(String schemaName, String tableName) {
    if (schemaName != null && !schemaName.isBlank()) {
      // Có thể wrap quote nếu cần, ví dụ: "\"" + schemaName + "\".\"" + tableName + "\""
      return schemaName + "." + tableName;
    }
    return tableName;
  }
}
