package tungnn.tutor.java.starter.infrastructure.translation.v1.document.impl;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import org.apache.poi.xslf.usermodel.*;
import tungnn.tutor.java.document.powerpoint.PowerPointUtil;
import tungnn.tutor.java.starter.infrastructure.translation.v2.document.AbstractDocumentTranslator;
import tungnn.tutor.java.starter.infrastructure.translation.v2.document.DocumentText;
import tungnn.tutor.java.starter.infrastructure.translation.v2.document.cache.TextTranslationCache;
import tungnn.tutor.java.starter.infrastructure.translation.v2.document.model.DocumentTranslationRequest;
import tungnn.tutor.java.starter.infrastructure.translation.v2.text.TextTranslator;

public class PowerPointDocumentTranslator extends AbstractDocumentTranslator<XMLSlideShow> {

  public PowerPointDocumentTranslator(
      TextTranslator textTranslator, TextTranslationCache textTranslationCache) {
    super(textTranslator, textTranslationCache);
  }

  @Override
  protected XMLSlideShow openDocument(DocumentTranslationRequest request) {
    var documentPath = request.documentPath();
    try (var in = Files.newInputStream(documentPath)) {
      return PowerPointUtil.readPresentation(in);
    } catch (IOException e) {
      throw new UncheckedIOException("Failed to open PowerPoint document: " + documentPath, e);
    }
  }

  @Override
  protected List<DocumentText> collectTexts(XMLSlideShow document) {
    var result = new ArrayList<DocumentText>();
    document.getSlides().forEach(slide -> collect(slide.getShapes(), result));
    return result;
  }

  private void collect(List<XSLFShape> shapes, List<DocumentText> out) {
    for (var shape : shapes) {
      switch (shape) {
        case XSLFGroupShape group -> collect(group.getShapes(), out);
        case XSLFTable table ->
            table
                .getRows()
                .forEach(row -> row.getCells().forEach(cell -> collectTextShape(cell, out)));
        case XSLFTextShape textShape -> collectTextShape(textShape, out);
        default -> {
          /* picture, connector, ...: bỏ qua */
        }
      }
    }
  }

  private void collectTextShape(XSLFTextShape textShape, List<DocumentText> out) {
    for (var paragraph : textShape.getTextParagraphs()) {
      for (var run : paragraph.getTextRuns()) {
        if (shouldTranslateValue(run.getRawText())) {
          out.add(new PptDocumentText(run));
        }
      }
    }
  }

  @Override
  protected void saveDocument(XMLSlideShow document, Path path) {
    try (var out = Files.newOutputStream(path)) {
      PowerPointUtil.writePresentation(document, out);
    } catch (IOException e) {
      throw new UncheckedIOException("Failed to save PowerPoint document: " + path, e);
    }
  }

  private record PptDocumentText(XSLFTextRun run) implements DocumentText {
    @Override
    public String getText() {
      var text = run.getRawText();
      return text == null ? "" : text.strip();
    }

    @Override
    public void setText(String text) {
      run.setText(text);
    }
  }
}
