package tungnn.tutor.java.starter.application.command;

import java.nio.file.Path;
import java.util.concurrent.Executors;
import tungnn.tutor.java.starter.application.service.impl.SimpleTranslationService;
import tungnn.tutor.java.starter.infrastructure.translation.v1.document.impl.ExcelDocumentTranslator;
import tungnn.tutor.java.starter.infrastructure.translation.v1.document.impl.SlideshowDocumentTranslator;
import tungnn.tutor.java.starter.infrastructure.translation.v1.document.impl.WordDocumentTranslator;
import tungnn.tutor.java.starter.infrastructure.translation.v1.text.DummyTextTranslator;
import tungnn.tutor.java.starter.infrastructure.translation.v1.text.orchestrator.DefaultTextTranslatorOrchestrator;

public class TranslationCommand {

  static void main(String[] args) {
    var sourcePath = Path.of(args[0]);
    var languageCode = args[1];

    var textTranslator = new DummyTextTranslator();

    var chunkSize = 50;
    var executor = Executors.newVirtualThreadPerTaskExecutor();
    var maxConcurrency = 10;
    var textTranslatorOrchestrator =
        new DefaultTextTranslatorOrchestrator(textTranslator, chunkSize, executor, maxConcurrency);

    var wordDocumentTranslator = new WordDocumentTranslator(textTranslatorOrchestrator);
    var excelDocumentTranslator = new ExcelDocumentTranslator(textTranslatorOrchestrator);
    var slideshowDocumentTranslator = new SlideshowDocumentTranslator(textTranslatorOrchestrator);

    var translationService =
        new SimpleTranslationService(
            wordDocumentTranslator, excelDocumentTranslator, slideshowDocumentTranslator);

    var result = translationService.translateDocument(sourcePath, languageCode);
  }
}
