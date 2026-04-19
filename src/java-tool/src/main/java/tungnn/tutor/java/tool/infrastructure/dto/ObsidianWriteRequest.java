package tungnn.tutor.java.tool.infrastructure.dto;

import java.nio.file.Path;

public record ObsidianWriteRequest(
    String title, String content, String url, String unitNo, Path resultDir) {}
