package tungnn.tutor.java.starter.infrastructure.translation.document.impl;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import javax.imageio.ImageIO;
import org.apache.pdfbox.pdmodel.PDDocument;
import tungnn.tutor.java.document.pdf.PdfDocumentUtil;
import tungnn.tutor.java.document.pdf.stripper.WordAwareStripper;
import tungnn.tutor.java.document.pdf.stripper.WordAwareStripper.WordChunk;
import tungnn.tutor.java.starter.infrastructure.translation.document.AbstractDocumentTranslator;
import tungnn.tutor.java.starter.infrastructure.translation.document.TextRef;
import tungnn.tutor.java.starter.infrastructure.translation.text.TextTranslator2;

public class PdfDocumentTranslator extends AbstractDocumentTranslator {

  private final WordAwareStripper stripper;

  private final Map<Path, List<PdfTextRef>> chunkRefs = new ConcurrentHashMap<>();

  public PdfDocumentTranslator(TextTranslator2 textTranslator, WordAwareStripper stripper) {
    super(textTranslator);
    this.stripper = stripper;
  }

  @Override
  protected void openDocument(Path docPath) {
    try (var in = Files.newInputStream(docPath)) {
      documentMap.put(docPath, PdfDocumentUtil.readDocument(in));
    } catch (IOException e) {
      throw new UncheckedIOException("Failed to open PDF document: " + docPath, e);
    }
  }

  @Override
  protected List<TextRef> collectTextRefs(Path docPath) {
    var document = document(docPath);

    List<WordChunk> wordChunks;
    try {
      wordChunks = PdfDocumentUtil.extractWordChunk(document, stripper);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }

    var holders = wordChunks.stream().map(PdfTextRef::new).toList();
    chunkRefs.put(docPath, holders);

    return new ArrayList<>(holders);
  }

  @Override
  protected void saveDocument(Path docPath, Path translationPath) {
    var original = document(docPath);
    var translatedChunks =
        chunkRefs.getOrDefault(docPath, List.of()).stream().map(PdfTextRef::toChunk).toList();

    try {
      PdfDocumentUtil.writeDocument(translationPath, translatedChunks);

      try (var in = Files.newInputStream(translationPath);
          var translated = PdfDocumentUtil.readDocument(in)) {
        PdfDocumentUtil.mergeSideBySide(original, translated, translationPath);
      }
    } catch (IOException e) {
      throw new UncheckedIOException("Failed to save PDF document: " + translationPath, e);
    }
  }

  @Override
  protected Path getContextFile(Path docPath) {
    var original = document(docPath);
    try {
      var image = PdfDocumentUtil.convertToSingleImage(original, 150);
      var path = Files.createTempFile("pdf-context-file-", ".png");
      ImageIO.write(image, "PNG", path.toFile());
      return path;
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  protected void closeDocument(Path docPath) {
    chunkRefs.remove(docPath);
    super.closeDocument(docPath);
  }

  private PDDocument document(Path docPath) {
    return (PDDocument) documentMap.get(docPath);
  }

  record PdfTextRef(WordChunk source, String[] holder) implements TextRef {
    PdfTextRef(WordChunk source) {
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
