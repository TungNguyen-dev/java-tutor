package tungnn.tutor.java.starter.infrastructure.translation.text;

import java.nio.file.Path;
import java.util.List;

public record TranslateChunkOption(Path context, List<Path> glossaryFiles) {}
