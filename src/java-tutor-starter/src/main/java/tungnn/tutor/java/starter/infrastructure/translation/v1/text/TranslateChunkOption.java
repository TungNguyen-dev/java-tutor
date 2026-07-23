package tungnn.tutor.java.starter.infrastructure.translation.v1.text;

import java.nio.file.Path;
import java.util.List;

public record TranslateChunkOption(Path context, List<Path> glossaryFiles) {}
