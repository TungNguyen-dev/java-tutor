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

public class PositionAwareStripper extends PDFTextStripper {

  private final List<TextChunk> chunks = new ArrayList<>();

  public PositionAwareStripper() throws IOException {
    super();
  }

  /**
   * Clears the chunk list at the start of each page so that {@link #getChunks()} always reflects
   * only the most recently processed page. Call {@link #getChunks()} after each page if you need
   * per-page results.
   */
  @Override
  protected void startPage(PDPage page) throws IOException {
    super.startPage(page);
    chunks.clear();
  }

  /**
   * {@inheritDoc}
   *
   * <p>Note: the {@code text} parameter is the normalized string for the whole word/run. Here we
   * iterate individual {@link TextPosition} objects to capture per-character position data. Glyphs
   * with no Unicode mapping (null/empty unicode) are skipped.
   */
  @Override
  protected void writeString(String text, List<TextPosition> textPositions) throws IOException {
    if (textPositions == null || textPositions.isEmpty()) {
      return;
    }

    for (TextPosition position : textPositions) {
      String unicode = position.getUnicode();
      if (unicode == null || unicode.isEmpty()) {
        // Glyph has no Unicode mapping; skip rather than throw NPE.
        continue;
      }

      chunks.add(
          new TextChunk(
              unicode,
              position.getX(), // screen-space x, origin upper-left
              position.getY(), // screen-space y, origin upper-left
              position.getFontSizeInPt(), // rendered pt size (Tf × Tm scaling)
              position.getFont(),
              position.getWidth(),
              position.getHeight() // heuristic max-height, see TextChunk Javadoc
              ));
    }
  }

  /**
   * Returns an unmodifiable view of the chunks collected from the most recently processed page. The
   * list is reset automatically at the start of each new page via {@link #startPage}.
   */
  public List<TextChunk> getChunks() {
    return Collections.unmodifiableList(chunks);
  }

  /**
   * Represents a single character (or ligature) extracted from a PDF page.
   *
   * <p>Coordinates (x, y) use screen space: origin at upper-left, adjusted for page rotation. They
   * are NOT raw PDF coordinates (which have origin at lower-left).
   *
   * <p>{@code fontSize} is the rendered size in pt (font size × text matrix scaling factor), NOT
   * the raw "Tf" operator value.
   *
   * <p>{@code height} is the heuristic max-height used by PDFBox text extraction, approximately 1/2
   * of the font bounding box height. It is NOT the exact glyph height.
   */
  public record TextChunk(
      String text, float x, float y, float fontSizePt, PDFont font, float width, float height) {

    public TextChunk {
      Objects.requireNonNull(text, "text cannot be null");
      Objects.requireNonNull(font, "font cannot be null");
      if (text.isEmpty()) throw new IllegalArgumentException("text cannot be empty");
    }
  }
}
