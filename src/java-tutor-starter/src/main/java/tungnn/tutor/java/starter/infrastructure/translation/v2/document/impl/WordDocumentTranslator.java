package tungnn.tutor.java.starter.infrastructure.translation.v2.document.impl;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import org.apache.poi.xwpf.usermodel.IBodyElement;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTP;
import tungnn.tutor.java.document.word.WordUtil;
import tungnn.tutor.java.starter.infrastructure.translation.v2.document.AbstractDocumentTranslator;
import tungnn.tutor.java.starter.infrastructure.translation.v2.document.DocumentText;
import tungnn.tutor.java.starter.infrastructure.translation.v2.document.cache.TextTranslationCache;
import tungnn.tutor.java.starter.infrastructure.translation.v2.document.model.DocumentTranslationRequest;
import tungnn.tutor.java.starter.infrastructure.translation.v2.text.TextTranslator;

public class WordDocumentTranslator extends AbstractDocumentTranslator<XWPFDocument> {

  public WordDocumentTranslator(
      TextTranslator textTranslator, TextTranslationCache textTranslationCache) {
    super(textTranslator, textTranslationCache);
  }

  @Override
  protected XWPFDocument openDocument(DocumentTranslationRequest request) {
    var docPath = request.documentPath();
    try (var in = Files.newInputStream(docPath)) {
      return WordUtil.readDocument(in);
    } catch (IOException e) {
      throw new UncheckedIOException("Failed to open Word document: " + docPath, e);
    }
  }

  @Override
  protected List<DocumentText> collectTexts(XWPFDocument document) {
    var result = new ArrayList<DocumentText>();

    collect(document.getBodyElements(), result); // body: paragraph + table
    document.getHeaderList().forEach(h -> collect(h.getBodyElements(), result));
    document.getFooterList().forEach(f -> collect(f.getBodyElements(), result));

    return result;
  }

  private void collect(List<IBodyElement> elements, List<DocumentText> out) {
    for (var element : elements) {
      switch (element) {
        case XWPFParagraph p -> {
          if (shouldTranslateParagraph(p)) {
            out.add(new WordTextRef(p));
          }
          collectTextBoxes(p, out);
        }
        case XWPFTable table ->
            table
                .getRows()
                .forEach(
                    row ->
                        row.getTableCells().forEach(cell -> collect(cell.getBodyElements(), out)));
        default -> {}
      }
    }
  }

  private void collectTextBoxes(XWPFParagraph paragraph, List<DocumentText> out) {
    try (var cursor = paragraph.getCTP().newCursor()) {
      cursor.selectPath(
          "declare namespace w='http://schemas.openxmlformats.org/wordprocessingml/2006/main' "
              + ".//w:txbxContent/w:p");

      var ctps = new ArrayList<CTP>();
      while (cursor.toNextSelection()) {
        if (cursor.getObject() instanceof CTP ctp) {
          ctps.add(ctp);
        }
      }

      for (var ctp : ctps) {
        var boxParagraph = new XWPFParagraph(ctp, paragraph.getBody());
        if (shouldTranslateParagraph(boxParagraph)) {
          out.add(new WordTextRef(boxParagraph));
        }
      }
    }
  }

  private boolean shouldTranslateParagraph(XWPFParagraph paragraph) {
    var text = paragraph.getText();
    return text != null && !text.isBlank();
  }

  @Override
  protected void saveDocument(XWPFDocument document, Path path) {
    try (var out = Files.newOutputStream(path)) {
      WordUtil.writeDocument(document, out);
    } catch (IOException e) {
      throw new UncheckedIOException("Failed to save Word document: " + path, e);
    }
  }

  private record WordTextRef(XWPFParagraph paragraph) implements DocumentText {
    @Override
    public String getText() {
      var text = paragraph.getText();
      return text == null ? "" : text.strip();
    }

    @Override
    public void setText(String text) {
      var runs = paragraph.getRuns();
      if (runs.isEmpty()) {
        paragraph.createRun().setText(text, 0);
        return;
      }
      runs.getFirst().setText(text, 0);
      for (int i = runs.size() - 1; i > 0; i--) {
        paragraph.removeRun(i);
      }
    }
  }
}
