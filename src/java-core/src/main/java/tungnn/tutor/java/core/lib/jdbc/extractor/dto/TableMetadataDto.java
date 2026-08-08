package tungnn.tutor.java.core.lib.jdbc.extractor.dto;

import java.util.List;
import java.util.Map;

public record TableMetadataDto(
    String tableName,
    String tableType, // TABLE, VIEW, etc.
    String remarks,
    Map<String, ColumnDto> columns,
    List<String> primaryKeys,
    List<ForeignKeyDto> foreignKeys,
    List<IndexDto> indexes) {
  public TableMetadataDto {
    columns = columns != null ? Map.copyOf(columns) : Map.of();
    primaryKeys = primaryKeys != null ? List.copyOf(primaryKeys) : List.of();
    foreignKeys = foreignKeys != null ? List.copyOf(foreignKeys) : List.of();
    indexes = indexes != null ? List.copyOf(indexes) : List.of();
  }
}
