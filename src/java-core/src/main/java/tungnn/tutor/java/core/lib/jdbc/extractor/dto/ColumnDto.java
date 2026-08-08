package tungnn.tutor.java.core.lib.jdbc.extractor.dto;

public record ColumnDto(
    String columnName,
    String dataType,
    int columnSize,
    boolean isNullable,
    String defaultValue,
    boolean isAutoIncrement,
    int ordinalPosition,
    String remarks) {}
