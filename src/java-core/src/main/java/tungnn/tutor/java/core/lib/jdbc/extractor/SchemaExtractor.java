package tungnn.tutor.java.core.lib.jdbc.extractor;

import java.sql.Connection;
import tungnn.tutor.java.core.lib.jdbc.extractor.dto.SchemaMetadataDto;

public interface SchemaExtractor {

  SchemaMetadataDto extractSchema(Connection connection, String catalog, String schema);
}
