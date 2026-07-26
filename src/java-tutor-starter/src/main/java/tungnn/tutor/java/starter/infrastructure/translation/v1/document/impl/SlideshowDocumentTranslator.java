package tungnn.tutor.java.starter.infrastructure.translation.v1.document.impl;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import org.apache.poi.xslf.usermodel.XMLSlideShow;
import org.apache.poi.xslf.usermodel.XSLFTableCell;
import org.apache.poi.xslf.usermodel.XSLFTextShape;
import tungnn.tutor.java.document.DocumentSemanticUnitCollector;
import tungnn.tutor.java.document.office.powerpoint.PowerPointUtil;
import tungnn.tutor.java.starter.infrastructure.translation.v1.document.AbstractDocumentTranslator;
import tungnn.tutor.java.starter.infrastructure.translation.v1.document.DocumentTranslationRequest;
import tungnn.tutor.java.starter.infrastructure.translation.v1.shared.TextReference;
import tungnn.tutor.java.starter.infrastructure.translation.v1.text.orchestrator.TextTranslatorOrchestrator;

public class SlideshowDocumentTranslator extends AbstractDocumentTranslator<XMLSlideShow> {

  public SlideshowDocumentTranslator(TextTranslatorOrchestrator translatorOrchestrator) {
    super(translatorOrchestrator);
  }

  @Override
  protected XMLSlideShow openDocument(DocumentTranslationRequest request) {
    try {
      return PowerPointUtil.readPresentation(Files.newInputStream(request.documentPath()));
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  protected List<TextReference> collectTextReferences(XMLSlideShow document) {
    var refs = new ArrayList<TextReference>();

    DocumentSemanticUnitCollector.collectTextShapes(document).stream()
        .map(TextShapeTextReference::new)
        .forEach(refs::add);

    DocumentSemanticUnitCollector.collectTableCells(document).stream()
        .map(TableCellTextReference::new)
        .forEach(refs::add);

    return refs;
  }

  @Override
  protected void saveDocument(XMLSlideShow document, Path translationPath) {
    try {
      PowerPointUtil.writePresentation(document, Files.newOutputStream(translationPath));
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  private record TextShapeTextReference(XSLFTextShape shape) implements TextReference {

    @Override
    public String getId() {
      return "shape-" + System.identityHashCode(shape);
    }

    @Override
    public String getText() {
      return shape.getText();
    }

    @Override
    public void setText(String text) {
      shape.clearText();
      shape.setText(text);
    }
  }

  private record TableCellTextReference(XSLFTableCell cell) implements TextReference {

    @Override
    public String getId() {
      return "table-cell-" + System.identityHashCode(cell);
    }

    @Override
    public String getText() {
      return cell.getText();
    }

    @Override
    public void setText(String text) {
      cell.clearText();
      cell.setText(text);
    }
  }
}
