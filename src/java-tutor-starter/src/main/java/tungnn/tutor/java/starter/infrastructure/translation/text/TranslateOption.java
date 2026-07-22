package tungnn.tutor.java.starter.infrastructure.translation.text;

import java.nio.file.Path;
import java.util.List;

public record TranslateOption(String context, List<Path> glossaryFiles) {}
