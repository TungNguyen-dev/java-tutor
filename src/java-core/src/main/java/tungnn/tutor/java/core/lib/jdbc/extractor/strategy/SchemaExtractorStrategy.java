package tungnn.tutor.java.core.lib.jdbc.extractor.strategy;

import java.sql.Connection;
import java.sql.SQLException;
import tungnn.tutor.java.core.lib.jdbc.extractor.dto.SchemaMetadataDto;

public interface SchemaExtractorStrategy {

  /** Kiểm tra xem Strategy này có hỗ trợ Database DatabaseProductName / Driver hiện tại không. */
  boolean supports(Connection connection) throws SQLException;

  /** Bắt đầu trích xuất toàn bộ Schema Metadata từ cấp Schema -> Tables -> Detail Elements */
  SchemaMetadataDto extractSchema(Connection connection, String catalog, String schema)
      throws SQLException;
}
