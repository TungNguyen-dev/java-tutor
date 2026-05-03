package tungnn.tutor.java.tool.v1.domain.dto;

import java.nio.file.Path;

public record ExtractTextRequest(Path imagePath, Path resultDir) {}
