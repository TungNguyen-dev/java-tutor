package tungnn.tutor.java.starter.infrastructure.translation.document.impl;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import org.apache.poi.xslf.usermodel.*;
import tungnn.tutor.java.document.powerpoint.PowerPointUtil;
import tungnn.tutor.java.starter.infrastructure.translation.document.AbstractDocumentTranslator;
import tungnn.tutor.java.starter.infrastructure.translation.document.TextRef;
import tungnn.tutor.java.starter.infrastructure.translation.text.TextTranslator2;

public class PowerPointDocumentTranslator extends AbstractDocumentTranslator {

  protected PowerPointDocumentTranslator(TextTranslator2 textTranslator) {
    super(textTranslator);
  }

  @Override
  protected void openDocument(Path docPath) {
    try (var in = Files.newInputStream(docPath)) {
      documentMap.put(docPath, PowerPointUtil.readPresentation(in));
    } catch (IOException e) {
      throw new UncheckedIOException("Failed to open PowerPoint document: " + docPath, e);
    }
  }

  @Override
  protected void saveDocument(Path docPath, Path translationPath) {
    var presentation = presentation(docPath);
    try (var out = Files.newOutputStream(translationPath)) {
      PowerPointUtil.writePresentation(presentation, out);
    } catch (IOException e) {
      throw new UncheckedIOException("Failed to save PowerPoint document: " + translationPath, e);
    }
  }

  @Override
  protected List<TextRef> collectTextRefs(Path docPath) {
    var presentation = presentation(docPath);
    var result = new ArrayList<TextRef>();
    presentation.getSlides().forEach(slide -> collect(slide.getShapes(), result));
    return result;
  }

  private void collect(List<XSLFShape> shapes, List<TextRef> out) {
    for (var shape : shapes) {
      switch (shape) {
        case XSLFGroupShape group -> collect(group.getShapes(), out); // đệ quy: nhóm shape
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

  private void collectTextShape(XSLFTextShape textShape, List<TextRef> out) {
    for (var paragraph : textShape.getTextParagraphs()) {
      for (var run : paragraph.getTextRuns()) {
        if (shouldTranslateValue(run.getRawText())) {
          out.add(new PptTextRef(run));
        }
      }
    }
  }

  @Override
  protected Path getContextFile(Path docPath) {
    var presentation = presentation(docPath);
    var context = PowerPointUtil.extractAllText(presentation);
    try {
      var path = Files.createTempFile("powerpoint-context-file-", ".txt");
      Files.writeString(path, context);
      return path;
    } catch (IOException e) {
      throw new UncheckedIOException("Failed to create PowerPoint context file for: " + docPath, e);
    }
  }

  private boolean shouldTranslateValue(String value) {
    return value != null && !value.isBlank();
  }

  private boolean hasLetter(String value) {
    return value.codePoints().anyMatch(Character::isLetter);
  }

  private XMLSlideShow presentation(Path docPath) {
    return (XMLSlideShow) documentMap.get(docPath);
  }

  record PptTextRef(XSLFTextRun run) implements TextRef {
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
