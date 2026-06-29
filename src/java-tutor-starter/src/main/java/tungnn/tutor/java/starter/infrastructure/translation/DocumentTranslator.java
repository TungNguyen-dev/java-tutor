package tungnn.tutor.java.starter.infrastructure.translation;

import java.nio.file.Path;

public interface DocumentTranslator {

  Path translateDocument(Path docPath);
}
