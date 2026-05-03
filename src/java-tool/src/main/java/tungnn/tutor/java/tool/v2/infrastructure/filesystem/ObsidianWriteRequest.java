package tungnn.tutor.java.tool.v2.infrastructure.filesystem;

import java.nio.file.Path;

public record ObsidianWriteRequest(
    String title, String content, String url, String unitNo, Path resultDir) {}
