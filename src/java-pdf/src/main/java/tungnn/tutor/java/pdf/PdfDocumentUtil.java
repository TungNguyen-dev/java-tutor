package tungnn.tutor.java.pdf;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.io.RandomAccessReadBuffer;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType0Font;
import org.apache.pdfbox.util.Matrix;
import tungnn.tutor.java.pdf.stripper.PositionAwareStripper;
import tungnn.tutor.java.pdf.stripper.WordAwareStripper;

public class PdfDocumentUtil {

  public static PDDocument readDocument(InputStream inputStream) throws IOException {
    return Loader.loadPDF(new RandomAccessReadBuffer(inputStream));
  }

  public static List<PositionAwareStripper.TextChunk> extractTextChunk(
      PDDocument document, PositionAwareStripper stripper) throws IOException {

    List<PositionAwareStripper.TextChunk> tokens = new ArrayList<>();

    stripper.setSortByPosition(true);
    int totalPages = document.getNumberOfPages();
    for (int i = 1; i <= totalPages; i++) {
      stripper.setStartPage(i);
      stripper.setEndPage(i);
      stripper.getText(document);
      tokens.addAll(stripper.getChunks());
    }

    return tokens;
  }

  public static List<WordAwareStripper.WordChunk> extractWordChunk(
      PDDocument document, WordAwareStripper stripper) throws IOException {

    List<WordAwareStripper.WordChunk> tokens = new ArrayList<>();

    stripper.setSortByPosition(true);
    int totalPages = document.getNumberOfPages();
    for (int i = 1; i <= totalPages; i++) {
      stripper.setStartPage(i);
      stripper.setEndPage(i);
      stripper.getText(document);
      tokens.addAll(stripper.getChunks());
    }

    return tokens;
  }

  public static void writeDocument(Path path, List<WordAwareStripper.WordChunk> chunks)
      throws IOException {

    if (chunks == null || chunks.isEmpty()) {
      return;
    }

    // --- 1. Group chunks by page number (preserving order) -----------------
    Map<Integer, List<WordAwareStripper.WordChunk>> byPage = new TreeMap<>();
    for (WordAwareStripper.WordChunk chunk : chunks) {
      byPage.computeIfAbsent(chunk.pageNo(), k -> new ArrayList<>()).add(chunk);
    }

    try (PDDocument doc = new PDDocument()) {

      PDFont font;
      try (InputStream fontStream =
          PDFont.class.getResourceAsStream(
              "/org/apache/pdfbox/resources/ttf/LiberationSans-Regular.ttf")) {
        font = PDType0Font.load(doc, fontStream, false);
      }

      final float margin = 20f;

      // --- 2. One PDPage per logical page ------------------------------------
      for (Map.Entry<Integer, List<WordAwareStripper.WordChunk>> entry : byPage.entrySet()) {
        List<WordAwareStripper.WordChunk> pageChunks = entry.getValue();

        // Derive page dimensions from this page's chunks only
        float maxX = 0f;
        float maxY = 0f;
        for (WordAwareStripper.WordChunk chunk : pageChunks) {
          maxX = Math.max(maxX, chunk.x() + chunk.width());
          maxY = Math.max(maxY, chunk.y() + chunk.height());
        }
        final float pageWidth = maxX + margin;
        final float pageHeight = maxY + margin;

        PDPage page = new PDPage(new PDRectangle(pageWidth, pageHeight));
        doc.addPage(page);

        // --- 3. Write each word at its absolute position -------------------
        try (PDPageContentStream cs = new PDPageContentStream(doc, page)) {
          for (WordAwareStripper.WordChunk chunk : pageChunks) {
            float pdfX = chunk.x();
            float pdfY = pageHeight - chunk.y();

            cs.beginText();
            cs.setFont(font, chunk.fontSizePt());
            cs.setTextMatrix(new Matrix(1, 0, 0, 1, pdfX, pdfY));
            cs.showText(chunk.text());
            cs.endText();
          }
        }
      }

      doc.save(path.toFile());
    }
  }
}
