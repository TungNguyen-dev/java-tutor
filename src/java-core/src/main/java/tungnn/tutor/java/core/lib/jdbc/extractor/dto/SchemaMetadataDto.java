package tungnn.tutor.java.core.lib.jdbc.extractor.dto;

import java.util.Map;

public record SchemaMetadataDto(
    String catalog, String schemaName, Map<String, TableMetadataDto> tables) {
  public SchemaMetadataDto {
    tables = tables != null ? Map.copyOf(tables) : Map.of();
  }
}
