package tungnn.tutor.java.starter.infrastructure.translation;

import java.nio.file.Path;
import java.time.Instant;
import tungnn.tutor.java.core.lib.io.filesystem.FileNameUtil;

public abstract class AbstractDocumentTranslator implements DocumentTranslator {

  protected final TextTranslator textTranslator;

  protected AbstractDocumentTranslator(TextTranslator textTranslator) {
    this.textTranslator = textTranslator;
  }

  @Override
  public Path translateDocument(Path docPath) {
    try {
      return doTranslate(docPath);
    } catch (Exception e) {
      throw new RuntimeException("Failed to translate document: " + docPath, e);
    }
  }

  protected abstract Path doTranslate(Path path) throws Exception;

  protected Path getOutputPath(Path path) {
    return path.getParent()
        .resolve(
            FileNameUtil.appendFilenameSuffix(
                path.getFileName().toString(), "_translated_" + Instant.now().toEpochMilli()));
  }
}
