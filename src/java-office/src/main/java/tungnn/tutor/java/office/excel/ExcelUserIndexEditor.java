package tungnn.tutor.java.office.excel;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellReference;

public class ExcelUserIndexEditor {

  private ExcelUserIndexEditor() {}

  // ==========================================
  // THAO TÁC THEO SỐ (1-BASED INDEX)
  // ==========================================

  public static Row cloneRow(Sheet sheet, int rowNum, int templateRowNum) {
    return ExcelEditor.cloneRow(sheet, rowNum - 1, templateRowNum - 1);
  }

  public static Row insertRow(Sheet sheet, int templateRowNum) {
    return ExcelEditor.insertRow(sheet, templateRowNum - 1);
  }

  public static Cell getOrCreateCell(Sheet sheet, int rowNum, int colNum) {
    Row row = sheet.getRow(rowNum - 1);
    if (row == null) row = sheet.createRow(rowNum - 1);

    Cell cell = row.getCell(colNum - 1);
    if (cell == null) cell = row.createCell(colNum - 1);

    return cell;
  }

  // =========================================================================
  // CELL REFERENCE METHODS (Get/Set by "A1", "B5")
  // =========================================================================

  /**
   * Gets a cell by its Excel reference address (e.g., "A1", "C5"). Creates it if it doesn't exist.
   */
  public static Cell getOrCreateCellByAddress(Sheet sheet, String address) {
    CellReference ref = new CellReference(address);
    Row row = sheet.getRow(ref.getRow());
    if (row == null) {
      row = sheet.createRow(ref.getRow());
    }
    Cell cell = row.getCell(ref.getCol());
    if (cell == null) {
      cell = row.createCell(ref.getCol());
    }
    return cell;
  }

  /** Sets a value to a cell by its Excel reference address, leveraging ExcelUtil data handling */
  public static void setCellValueByAddress(Sheet sheet, String address, Object value) {
    CellReference ref = new CellReference(address);
    Row row = sheet.getRow(ref.getRow());
    if (row == null) {
      row = sheet.createRow(ref.getRow());
    }
    ExcelUtil.createCell(row, ref.getCol(), value);
  }

  /** Gets content from a cell by its Excel reference address */
  public static Object getCellValueByAddress(Sheet sheet, String address) {
    CellReference ref = new CellReference(address);
    Row row = sheet.getRow(ref.getRow());
    if (row == null) return null;
    Cell cell = row.getCell(ref.getCol());
    return ExcelUtil.getCellContent(cell);
  }
}
