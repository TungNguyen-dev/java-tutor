package tungnn.tutor.java.tool.domain.dto;

import java.nio.file.Path;

public record ExtractTextRequest(Path imagePath, Path resultDir) {}
