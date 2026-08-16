package tungnn.tutor.java.tool.translation.service.impl;

import java.nio.file.Path;
import tungnn.tutor.java.core.lib.io.filesystem.FileNameUtil;
import tungnn.tutor.java.tool.translation.core.document.DocumentTranslationRequest;
import tungnn.tutor.java.tool.translation.core.document.impl.ExcelDocumentTranslator;
import tungnn.tutor.java.tool.translation.core.document.impl.SlideshowDocumentTranslator;
import tungnn.tutor.java.tool.translation.core.document.impl.WordDocumentTranslator;
import tungnn.tutor.java.tool.translation.service.TranslationService;
import tungnn.tutor.java.tool.translation.shared.LanguageCode;

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
