package tungnn.tutor.java.core.lib.jdbc.extractor.dto;

import java.util.List;

public record IndexDto(String indexName, boolean isUnique, List<String> columnNames) {}
