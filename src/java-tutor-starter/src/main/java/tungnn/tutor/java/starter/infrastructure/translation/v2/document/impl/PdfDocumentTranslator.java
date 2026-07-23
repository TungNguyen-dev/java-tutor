package tungnn.tutor.java.starter.infrastructure.translation.v2.document.impl;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.apache.pdfbox.pdmodel.PDDocument;
import tungnn.tutor.java.document.pdf.PdfDocumentUtil;
import tungnn.tutor.java.document.pdf.stripper.WordAwareStripper;
import tungnn.tutor.java.document.pdf.stripper.WordAwareStripper.WordChunk;
import tungnn.tutor.java.starter.infrastructure.translation.v2.document.AbstractDocumentTranslator;
import tungnn.tutor.java.starter.infrastructure.translation.v2.document.DocumentText;
import tungnn.tutor.java.starter.infrastructure.translation.v2.document.cache.TextTranslationCache;
import tungnn.tutor.java.starter.infrastructure.translation.v2.document.model.DocumentTranslationRequest;
import tungnn.tutor.java.starter.infrastructure.translation.v2.text.TextTranslator;

public class PdfDocumentTranslator extends AbstractDocumentTranslator<PDDocument> {

  private final Map<Long, List<PdfDocumentText>> documentTextListMap = new ConcurrentHashMap<>();
  private final WordAwareStripper stripper;

  public PdfDocumentTranslator(
      TextTranslator textTranslator,
      TextTranslationCache textTranslationCache,
      WordAwareStripper stripper) {

    super(textTranslator, textTranslationCache);
    this.stripper = stripper;
  }

  @Override
  protected PDDocument openDocument(DocumentTranslationRequest request) {
    try (var in = Files.newInputStream(request.documentPath())) {
      return PdfDocumentUtil.readDocument(in);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  protected List<DocumentText> collectTexts(PDDocument document) {
    List<WordChunk> wordChunks;
    try {
      wordChunks = PdfDocumentUtil.extractWordChunk(document, stripper);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }

    var holders = wordChunks.stream().map(PdfDocumentText::new).toList();
    documentTextListMap.put(document.getDocumentId(), holders);

    return new ArrayList<>(holders);
  }

  @Override
  protected void saveDocument(PDDocument document, Path path) {
    var translatedChunks =
        documentTextListMap.getOrDefault(document.getDocumentId(), List.of()).stream()
            .map(PdfDocumentText::toChunk)
            .toList();

    try {
      PdfDocumentUtil.writeDocument(path, translatedChunks);

      try (var in = Files.newInputStream(path);
          var translated = PdfDocumentUtil.readDocument(in)) {
        PdfDocumentUtil.mergeSideBySide(document, translated, path);
      }
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  private record PdfDocumentText(WordChunk source, String[] holder) implements DocumentText {

    PdfDocumentText(WordChunk source) {
      this(source, new String[] {source.text()});
    }

    @Override
    public String getText() {
      return source.text();
    }

    @Override
    public void setText(String text) {
      holder[0] = text;
    }

    WordChunk toChunk() {
      return new WordChunk(
          holder[0],
          source.pageNo(),
          source.x(),
          source.y(),
          source.fontSizePt(),
          source.font(),
          source.width(),
          source.height());
    }
  }
}
