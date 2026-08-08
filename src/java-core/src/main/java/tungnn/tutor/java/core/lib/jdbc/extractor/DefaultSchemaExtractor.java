package tungnn.tutor.java.core.lib.jdbc.extractor;

import java.sql.Connection;
import java.util.List;
import tungnn.tutor.java.core.lib.jdbc.extractor.dto.SchemaMetadataDto;
import tungnn.tutor.java.core.lib.jdbc.extractor.strategy.GenericJdbcSchemaStrategy;
import tungnn.tutor.java.core.lib.jdbc.extractor.strategy.OracleSchemaStrategy;
import tungnn.tutor.java.core.lib.jdbc.extractor.strategy.PostgresSchemaStrategy;
import tungnn.tutor.java.core.lib.jdbc.extractor.strategy.SchemaExtractorStrategy;

public class DefaultSchemaExtractor implements SchemaExtractor {

  private final List<SchemaExtractorStrategy> strategies;
  private final SchemaExtractorStrategy fallbackStrategy;

  public DefaultSchemaExtractor() {
    // Thứ tự ưu tiên: Vendor-specific đứng trước, Generic đứng cuối
    this.strategies = List.of(new OracleSchemaStrategy(), new PostgresSchemaStrategy());
    this.fallbackStrategy = new GenericJdbcSchemaStrategy();
  }

  @Override
  public SchemaMetadataDto extractSchema(Connection connection, String catalog, String schema) {
    try {
      for (SchemaExtractorStrategy strategy : strategies) {
        if (strategy.supports(connection)) {
          return strategy.extractSchema(connection, catalog, schema);
        }
      }
      return fallbackStrategy.extractSchema(connection, catalog, schema);
    } catch (Exception e) {
      System.err.println("Error while trying to fallback strategy: " + e.getMessage());
      throw new RuntimeException(e);
    }
  }
}
