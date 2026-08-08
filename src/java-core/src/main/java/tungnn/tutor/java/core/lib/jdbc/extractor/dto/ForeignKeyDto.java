package tungnn.tutor.java.core.lib.jdbc.extractor.dto;

public record ForeignKeyDto(
    String fkName, String fkColumnName, String pkTableName, String pkColumnName) {}
