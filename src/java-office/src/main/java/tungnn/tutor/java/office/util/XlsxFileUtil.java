package tungnn.tutor.java.office.util;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Iterator;

public final class XlsxFileUtil {

  private static final DataFormatter dataFormatter = new DataFormatter();

  private XlsxFileUtil() {
    // Utility class
  }

  /* =========================
   * Workbook level
   * ========================= */

  public static String readWorkbookAsString(Workbook workbook) {
    StringBuilder sb = new StringBuilder();

    for (Sheet sheet : workbook) {
      sb.append("Sheet: ").append(sheet.getSheetName()).append(System.lineSeparator());
      sb.append(readSheetAsString(sheet)).append(System.lineSeparator());
    }

    return sb.toString();
  }

  public static String readWorkbookAsString(Path source) throws IOException {
    try (InputStream in = Files.newInputStream(source);
        Workbook workbook = new XSSFWorkbook(in)) {
      return readWorkbookAsString(workbook);
    }
  }

  public static void writeWorkbook(Path target, Workbook workbook) {
    try (OutputStream os = Files.newOutputStream(target)) {
      workbook.write(os);
    } catch (IOException e) {
      throw new IllegalStateException("Failed to write workbook to: " + target, e);
    }
  }

  /* =========================
   * Sheet level
   * ========================= */

  public static String readSheetAsString(Sheet sheet) {
    StringBuilder sb = new StringBuilder();

    for (Row row : sheet) {
      sb.append(readRowAsString(row)).append(System.lineSeparator());
    }

    return sb.toString();
  }

  /* =========================
   * Row level
   * ========================= */

  public static String readRowAsString(Row row) {
    StringBuilder sb = new StringBuilder();

    Iterator<Cell> cellIterator = row.cellIterator();
    while (cellIterator.hasNext()) {
      Cell cell = cellIterator.next();
      sb.append(readCellAsRawString(cell)).append('\t');
    }

    return sb.toString().trim();
  }

  /* =========================
   * Cell level
   * ========================= */

  public static String readCellAsRawString(Cell cell) {
    if (cell == null) {
      return "";
    }

    return switch (cell.getCellType()) {
      case STRING -> cell.getStringCellValue();
      case NUMERIC ->
          DateUtil.isCellDateFormatted(cell)
              ? cell.getLocalDateTimeCellValue().toString()
              : Double.toString(cell.getNumericCellValue());
      case BOOLEAN -> Boolean.toString(cell.getBooleanCellValue());
      case FORMULA -> cell.getCellFormula();
      default -> "";
    };
  }

  public static String readCellAsDisplayString(Cell cell) {
    FormulaEvaluator evaluator =
        cell.getSheet().getWorkbook().getCreationHelper().createFormulaEvaluator();
    return dataFormatter.formatCellValue(cell, evaluator);
  }
}
