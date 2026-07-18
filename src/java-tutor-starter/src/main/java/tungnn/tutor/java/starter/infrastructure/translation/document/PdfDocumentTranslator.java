package tungnn.tutor.java.starter.infrastructure.translation.document;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import tungnn.tutor.java.document.pdf.PdfDocumentUtil;
import tungnn.tutor.java.document.pdf.stripper.WordAwareStripper;
import tungnn.tutor.java.document.pdf.stripper.WordAwareStripper.WordChunk;
import tungnn.tutor.java.starter.infrastructure.translation.shared.LanguageCode;
import tungnn.tutor.java.starter.infrastructure.translation.text.TextTranslation;
import tungnn.tutor.java.starter.infrastructure.translation.text.TextTranslator;

public class PdfDocumentTranslator extends AbstractDocumentTranslator {

  private final WordAwareStripper stripper;

  public PdfDocumentTranslator(TextTranslator textTranslator, WordAwareStripper stripper) {
    super(textTranslator);
    this.stripper = stripper;
  }

  @Override
  protected void doTranslate(Path inputPath, Path outputPath, LanguageCode targetLanguage)
      throws IOException {

    try (var inputStream = Files.newInputStream(inputPath);
        var document = PdfDocumentUtil.readDocument(inputStream)) {

      var wordChunks = PdfDocumentUtil.extractWordChunk(document, stripper);
      var translations = translate(wordChunks, targetLanguage);

      var translatedChunks =
          wordChunks.stream()
              .map(chunk -> withTranslatedText(chunk, translations.get(chunk.text())))
              .collect(Collectors.toList());
      PdfDocumentUtil.writeDocument(outputPath, translatedChunks);

      var documentTranslated = PdfDocumentUtil.readDocument(Files.newInputStream(outputPath));
      PdfDocumentUtil.mergeSideBySide(document, documentTranslated, outputPath);
    }
  }

  private Map<String, String> translate(List<WordChunk> chunks, LanguageCode targetLanguage) {
    var texts = chunks.stream().map(WordChunk::text).toList();
    return textTranslator.translate(texts, targetLanguage).stream()
        .collect(
            Collectors.toMap(
                TextTranslation::sourceText,
                TextTranslation::targetText,
                (existing, ignored) -> existing));
  }

  private WordChunk withTranslatedText(WordChunk chunk, String translatedText) {
    return new WordChunk(
        translatedText,
        chunk.pageNo(),
        chunk.x(),
        chunk.y(),
        chunk.fontSizePt(),
        chunk.font(),
        chunk.width(),
        chunk.height());
  }
}
