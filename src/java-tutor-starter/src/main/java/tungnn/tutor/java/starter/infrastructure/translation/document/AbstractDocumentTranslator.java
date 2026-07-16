package tungnn.tutor.java.starter.infrastructure.translation.document;

import java.io.IOException;
import java.nio.file.Path;
import java.time.Instant;
import tungnn.tutor.java.core.lib.io.filesystem.FileNameUtil;
import tungnn.tutor.java.starter.infrastructure.translation.shared.LanguageCode;
import tungnn.tutor.java.starter.infrastructure.translation.text.TextTranslator;

public abstract class AbstractDocumentTranslator implements DocumentTranslator {

  protected final TextTranslator textTranslator;

  protected AbstractDocumentTranslator(TextTranslator textTranslator) {
    this.textTranslator = textTranslator;
  }

  @Override
  public Path translateDocument(Path docPath, LanguageCode targetLanguage) {
    try {
      var outputPath = getOutputPath(docPath);

      doTranslate(docPath, outputPath, targetLanguage);

      return outputPath;
    } catch (Exception e) {
      System.err.println(e.getMessage());
      throw new RuntimeException("Error translating document", e);
    }
  }

  protected abstract void doTranslate(Path inputPath, Path outputPath, LanguageCode targetLanguage)
      throws IOException;

  protected Path getOutputPath(Path path) {
    return path.getParent()
        .resolve(
            FileNameUtil.appendFilenameSuffix(
                path.getFileName().toString(), "_translated_" + Instant.now().toEpochMilli()));
  }
}
