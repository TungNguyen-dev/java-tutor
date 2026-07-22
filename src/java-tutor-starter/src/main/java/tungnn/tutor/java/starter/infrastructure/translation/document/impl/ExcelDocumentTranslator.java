package tungnn.tutor.java.starter.infrastructure.translation.document.impl;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Workbook;
import tungnn.tutor.java.document.excel.ExcelUtil;
import tungnn.tutor.java.starter.infrastructure.translation.document.AbstractDocumentTranslator;
import tungnn.tutor.java.starter.infrastructure.translation.document.TextRef;
import tungnn.tutor.java.starter.infrastructure.translation.text.TextTranslator2;

public class ExcelDocumentTranslator extends AbstractDocumentTranslator {

  protected ExcelDocumentTranslator(TextTranslator2 textTranslator) {
    super(textTranslator);
  }

  @Override
  protected void openDocument(Path docPath) {
    try {
      documentMap.put(docPath, ExcelUtil.readWorkbook(docPath));
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  protected void saveDocument(Path docPath, Path translationPath) {
    var document = documentMap.get(docPath);
    try {
      ExcelUtil.writeWorkbook((Workbook) document, translationPath);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  protected List<TextRef> collectTextRefs(Path docPath) {
    var result = new ArrayList<TextRef>();
    var wb = (Workbook) documentMap.get(docPath);
    for (var sheet : wb) {
      for (var row : sheet) {
        for (var cell : row) {
          if (shouldNotTranslateCell(cell)) {
            continue;
          }
          var textRef = new ExcelTextRef(cell);
          result.add(textRef);
        }
      }
    }

    return result;
  }

  @Override
  protected Path getContextFile(Path docPath) {
    var wb = (Workbook) documentMap.get(docPath);
    var context = ExcelUtil.extractAllText(wb);
    try {
      var path = Files.createTempFile("excel-context-file-", ".txt");
      Files.writeString(path, context);
      return path;
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

  record ExcelTextRef(Cell cell) implements TextRef {
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
