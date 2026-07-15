package tungnn.tutor.java.pdf.stripper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.pdfbox.text.TextPosition;

/**
 * Extracts text at word level. Each call to writeString() by PDFBox's internal writeLine()
 * corresponds to exactly one word (between word separators).
 *
 * <p>Coordinates (x, y): screen space, origin upper-left, adjusted for page rotation. fontSizePt:
 * max rendered pt size across all characters in the word. width: bounding box = lastChar.x +
 * lastChar.width - firstChar.x height: max heuristic height across all characters (not exact glyph
 * height).
 */
public class WordAwareStripper extends PDFTextStripper {

  private final List<WordChunk> wordChunks = new ArrayList<>();

  public WordAwareStripper() throws IOException {
    super();
    // Bật sort để PDFBox detect word boundary tốt hơn,
    // đặc biệt với PDF encode từng ký tự riêng lẻ.
    setSortByPosition(true);
  }

  /** Reset word list at the start of each page. */
  @Override
  protected void startPage(PDPage page) throws IOException {
    super.startPage(page);
    wordChunks.clear();
  }

  /**
   * Called once per word by PDFBox's internal writeLine(). textPositions contains one TextPosition
   * per character (or ligature) in the word.
   */
  @Override
  protected void writeString(String text, List<TextPosition> textPositions) throws IOException {
    if (textPositions == null || textPositions.isEmpty() || text == null || text.isBlank()) {
      return;
    }

    TextPosition first = textPositions.getFirst();
    TextPosition last = textPositions.getLast();

    float x = first.getX();
    float y = first.getY();
    float width = last.getX() + last.getWidth() - first.getX();
    PDFont font = first.getFont();

    float fontSizePt = 0f;
    float height = 0f;
    for (TextPosition tp : textPositions) {
      fontSizePt = Math.max(fontSizePt, tp.getXScale());
      height = Math.max(height, tp.getHeight());
    }

    wordChunks.add(new WordChunk(text, getCurrentPageNo(), x, y, fontSizePt, font, width, height));
  }

  /** Returns word chunks from the most recently processed page. */
  public List<WordChunk> getChunks() {
    return Collections.unmodifiableList(wordChunks);
  }

  public record WordChunk(
      String text,
      int pageNo,
      float x,
      float y,
      float fontSizePt,
      PDFont font,
      float width,
      float height) {

    public WordChunk {
      Objects.requireNonNull(text, "text cannot be null");
      Objects.requireNonNull(font, "font cannot be null");
      if (text.isBlank()) throw new IllegalArgumentException("text cannot be blank");
      if (pageNo < 1) throw new IllegalArgumentException("pageNo must be >= 1");
    }
  }
}
