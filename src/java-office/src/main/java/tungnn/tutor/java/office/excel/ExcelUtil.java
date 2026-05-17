package tungnn.tutor.java.office.excel;

import org.apache.poi.extractor.ExtractorFactory;
import org.apache.poi.ss.extractor.ExcelExtractor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public final class ExcelUtil {

  private static final DataFormatter DATA_FORMATTER = new DataFormatter();

  private ExcelUtil() {}

  // Read workbook
  public static Workbook readWorkbook(Path path) throws IOException {
    try (var is = Files.newInputStream(path)) {
      return WorkbookFactory.create(is);
    }
  }

  // Text Extraction
  public static String extractText(Path path) throws IOException {
    if (path == null) {
      return "";
    }

    try (var is = Files.newInputStream(path);
        var genericExtractor = ExtractorFactory.createExtractor(is)) {

      if (genericExtractor instanceof ExcelExtractor extractor) {
        // Extract formula values instead of formula text
        extractor.setFormulasNotResults(false);
        extractor.setIncludeSheetNames(true);
        return extractor.getText();
      }
    }

    return "";
  }

  // Create new sheet: createSheet(String sheetName)
  public static Sheet createSheet(Workbook workbook, String sheetName) {
    return workbook.createSheet(sheetName);
  }

  // Clone new sheet: cloneSheet(String sheetName, String templateSheet)
  public static Sheet cloneSheet(Workbook workbook, String templateSheetName, String newSheetName) {
    var templateIndex = workbook.getSheetIndex(templateSheetName);
    if (templateIndex == -1) {
      throw new IllegalArgumentException("Template sheet not found: " + templateSheetName);
    }
    var clonedSheet = workbook.cloneSheet(templateIndex);
    var clonedIndex = workbook.getSheetIndex(clonedSheet);
    workbook.setSheetName(clonedIndex, newSheetName);
    return clonedSheet;
  }

  // Clone new row: cloneRow(int rowIndex, int templateRowIndex)
  public static Row cloneRow(Sheet sheet, int rowIndex, int templateRowIndex) {
    var sourceRow = sheet.getRow(templateRowIndex);
    var targetRow = sheet.getRow(rowIndex);

    if (targetRow != null) {
      sheet.removeRow(targetRow);
    }
    targetRow = sheet.createRow(rowIndex);

    if (sourceRow != null) {
      // Copy row height
      targetRow.setHeight(sourceRow.getHeight());

      // Copy cells
      for (var i = 0; i < sourceRow.getLastCellNum(); i++) {
        var sourceCell = sourceRow.getCell(i);
        if (sourceCell != null) {
          var targetCell = targetRow.createCell(i);
          copyCell(sourceCell, targetCell);
        }
      }

      // Copy merged regions belonging to this row
      for (var i = 0; i < sheet.getNumMergedRegions(); i++) {
        var cellRangeAddress = sheet.getMergedRegion(i);
        if (cellRangeAddress.getFirstRow() == sourceRow.getRowNum()) {
          var newCellRangeAddress =
              new CellRangeAddress(
                  targetRow.getRowNum(),
                  targetRow.getRowNum()
                      + (cellRangeAddress.getLastRow() - cellRangeAddress.getFirstRow()),
                  cellRangeAddress.getFirstColumn(),
                  cellRangeAddress.getLastColumn());
          sheet.addMergedRegion(newCellRangeAddress);
        }
      }
    }
    return targetRow;
  }

  // Insert and clone a row directly ABOVE the template row
  public static Row insertRowAbove(Sheet sheet, int templateRowIndex) {
    var lastRow = sheet.getLastRowNum();

    // 1. Shift rows down starting at the template index to make a gap
    if (templateRowIndex <= lastRow) {
      sheet.shiftRows(templateRowIndex, lastRow, 1, true, false);
    }

    // 2. The original template row has now moved down to (templateRowIndex + 1)
    var shiftedTemplateIndex = templateRowIndex + 1;

    // 3. Clone the shifted template back into the newly created gap
    return cloneRow(sheet, templateRowIndex, shiftedTemplateIndex);
  }

  // Insert and clone a row directly BELOW the template row
  public static Row insertRowBelow(Sheet sheet, int templateRowIndex) {
    var lastRow = sheet.getLastRowNum();
    var targetRowIndex = templateRowIndex + 1;

    // 1. Shift rows down starting right below the template index to make a gap
    if (targetRowIndex <= lastRow) {
      sheet.shiftRows(targetRowIndex, lastRow, 1, true, false);
    }

    // 2. The original template row did not move. Clone it into the new gap below it
    return cloneRow(sheet, targetRowIndex, templateRowIndex);
  }

  // Getting the cell contents as raw String
  public static String getCellRawString(Cell cell) {
    if (cell == null) {
      return "";
    }
    return switch (cell.getCellType()) {
      case BOOLEAN -> String.valueOf(cell.getBooleanCellValue());
      case NUMERIC -> String.valueOf(cell.getNumericCellValue());
      case STRING -> cell.getStringCellValue();
      case ERROR -> String.valueOf(cell.getErrorCellValue());
      case FORMULA -> cell.getCellFormula();
      default -> "";
    };
  }

  // Getting the cell contents as valued String (value and format it yourself)
  public static String getCellValueFormatted(Cell cell) {
    if (cell == null) {
      return "";
    }
    // DataFormatter evaluates formulas and applies styles automatically
    return DATA_FORMATTER.formatCellValue(cell);
  }

  // Helper method to copy cell properties and values
  private static void copyCell(Cell sourceCell, Cell targetCell) {
    targetCell.setCellStyle(sourceCell.getCellStyle());

    switch (sourceCell.getCellType()) {
      case BOOLEAN:
        targetCell.setCellValue(sourceCell.getBooleanCellValue());
        break;
      case ERROR:
        targetCell.setCellErrorValue(sourceCell.getErrorCellValue());
        break;
      case FORMULA:
        targetCell.setCellFormula(sourceCell.getCellFormula());
        break;
      case NUMERIC:
        targetCell.setCellValue(sourceCell.getNumericCellValue());
        break;
      case STRING:
        targetCell.setCellValue(sourceCell.getRichStringCellValue());
        break;
      case BLANK:
        targetCell.setBlank();
        break;
      default:
        break;
    }
  }
}
