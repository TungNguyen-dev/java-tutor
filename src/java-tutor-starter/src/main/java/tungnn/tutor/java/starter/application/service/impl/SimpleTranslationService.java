package tungnn.tutor.java.starter.application.service.impl;

import java.nio.file.Path;
import tungnn.tutor.java.core.lib.io.filesystem.FileNameUtil;
import tungnn.tutor.java.starter.application.service.TranslationService;
import tungnn.tutor.java.starter.infrastructure.translation.v1.document.DocumentTranslationRequest;
import tungnn.tutor.java.starter.infrastructure.translation.v1.document.impl.ExcelDocumentTranslator;
import tungnn.tutor.java.starter.infrastructure.translation.v1.document.impl.SlideshowDocumentTranslator;
import tungnn.tutor.java.starter.infrastructure.translation.v1.document.impl.WordDocumentTranslator;
import tungnn.tutor.java.starter.infrastructure.translation.v1.shared.LanguageCode;

public class SimpleTranslationService implements TranslationService {

  private final WordDocumentTranslator wordDocumentTranslator;
  private final ExcelDocumentTranslator excelDocumentTranslator;
  private final SlideshowDocumentTranslator slideshowDocumentTranslator;

  public SimpleTranslationService(
      WordDocumentTranslator wordDocumentTranslator,
      ExcelDocumentTranslator excelDocumentTranslator,
      SlideshowDocumentTranslator slideshowDocumentTranslator) {

    this.wordDocumentTranslator = wordDocumentTranslator;
    this.excelDocumentTranslator = excelDocumentTranslator;
    this.slideshowDocumentTranslator = slideshowDocumentTranslator;
  }

  @Override
  public Path translateDocument(Path sourcePath, String languageCode) {
    var request =
        new DocumentTranslationRequest(
            sourcePath, LanguageCode.valueOf(languageCode.toUpperCase()));
    var filename = sourcePath.getFileName().toString();
    var extension = FileNameUtil.parseExtension(filename);
    var response =
        switch (extension) {
          case "docx" -> wordDocumentTranslator.translate(request);
          case "xlsx" -> excelDocumentTranslator.translate(request);
          case "pptx" -> slideshowDocumentTranslator.translate(request);
          default -> throw new IllegalArgumentException("Unknown extension: " + extension);
        };
    return response.translationPath();
  }
}
