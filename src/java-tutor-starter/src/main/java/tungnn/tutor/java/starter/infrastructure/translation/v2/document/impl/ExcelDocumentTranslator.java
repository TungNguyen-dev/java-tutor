package tungnn.tutor.java.starter.infrastructure.translation.v2.document.impl;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Workbook;
import tungnn.tutor.java.document.excel.ExcelUtil;
import tungnn.tutor.java.starter.infrastructure.translation.v2.document.AbstractDocumentTranslator;
import tungnn.tutor.java.starter.infrastructure.translation.v2.document.DocumentText;
import tungnn.tutor.java.starter.infrastructure.translation.v2.document.cache.TextTranslationCache;
import tungnn.tutor.java.starter.infrastructure.translation.v2.document.model.DocumentTranslationRequest;
import tungnn.tutor.java.starter.infrastructure.translation.v2.text.TextTranslator;

public class ExcelDocumentTranslator extends AbstractDocumentTranslator<Workbook> {

  public ExcelDocumentTranslator(
      TextTranslator textTranslator, TextTranslationCache textTranslationCache) {
    super(textTranslator, textTranslationCache);
  }

  @Override
  protected Workbook openDocument(DocumentTranslationRequest request) {
    try {
      return ExcelUtil.readWorkbook(request.documentPath());
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  protected List<DocumentText> collectTexts(Workbook document) {
    var result = new ArrayList<DocumentText>();
    for (var sheet : document) {
      for (var row : sheet) {
        for (var cell : row) {
          if (shouldNotTranslateCell(cell)) {
            continue;
          }
          result.add(new ExcelDocumentText(cell));
        }
      }
    }

    return result;
  }

  @Override
  protected void saveDocument(Workbook document, Path path) {
    try {
      ExcelUtil.writeWorkbook((Workbook) document, path);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  private boolean shouldNotTranslateCell(Cell cell) {
    if (cell == null) {
      return true;
    }
    var type = cell.getCellType();
    if (type == CellType.STRING) {
      return false;
    }
    return type != CellType.FORMULA || cell.getCachedFormulaResultType() != CellType.STRING;
  }

  private record ExcelDocumentText(Cell cell) implements DocumentText {
    @Override
    public String getText() {
      return ExcelUtil.getCellContent(cell).toString().strip();
    }

    @Override
    public void setText(String text) {
      cell.setCellValue(text);
    }
  }
}
