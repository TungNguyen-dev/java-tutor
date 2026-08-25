package tungnn.tutor.java.tool.translation;

import java.nio.file.Path;
import java.util.concurrent.Executors;
import tungnn.tutor.java.tool.translation.config.AppConfig;
import tungnn.tutor.java.tool.translation.core.document.impl.ExcelDocumentTranslator;
import tungnn.tutor.java.tool.translation.core.document.impl.SlideshowDocumentTranslator;
import tungnn.tutor.java.tool.translation.core.document.impl.WordDocumentTranslator;
import tungnn.tutor.java.tool.translation.core.text.DummyTextTranslator;
import tungnn.tutor.java.tool.translation.core.text.orchestrator.DefaultTextTranslatorOrchestrator;
import tungnn.tutor.java.tool.translation.service.impl.SimpleTranslationService;

public class Application {

  static void main(String[] args) {
    if (args.length < 2) {
      System.err.println("Usage: java Application <sourcePath> <languageCode>");
      System.exit(1);
    }

    var config = AppConfig.load();

    var sourcePath = Path.of(args[0]);
    var languageCode = args[1];

    var textTranslator = new DummyTextTranslator();

    try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {
      var textTranslatorOrchestrator =
          new DefaultTextTranslatorOrchestrator(
              textTranslator, config.chunkSize(), executor, config.maxConcurrency());

      var wordDocumentTranslator = new WordDocumentTranslator(textTranslatorOrchestrator);
      var excelDocumentTranslator = new ExcelDocumentTranslator(textTranslatorOrchestrator);
      var slideshowDocumentTranslator = new SlideshowDocumentTranslator(textTranslatorOrchestrator);

      var translationService =
          new SimpleTranslationService(
              wordDocumentTranslator, excelDocumentTranslator, slideshowDocumentTranslator);

      var result = translationService.translateDocument(sourcePath, languageCode);
      System.out.println("Translation completed successfully: " + result);
    }
  }
}
