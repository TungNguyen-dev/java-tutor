package tungnn.tutor.java.tool.excel;

import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellReference;
import tungnn.tutor.java.document.office.excel.ExcelUtil;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class ExcelRefErrorFinder {

  /** Record to store details of cells containing #REF! errors */
  public record RefErrorDetail(String sheetName, String cellAddress, String formula) {
    @Override
    public String toString() {
      return String.format(
          "Sheet: %-15s | Cell: %-5s | Formula: %s", "'" + sheetName + "'", cellAddress, formula);
    }
  }

  /**
   * Scans an Excel file and returns a list of cells containing #REF! errors.
   *
   * @param excelFilePath Path to the Excel file (.xlsx or .xls)
   * @return List of details for cells with #REF! errors
   */
  public static List<RefErrorDetail> findRefErrorCells(Path excelFilePath) {
    List<RefErrorDetail> errorCells = new ArrayList<>();

    try (Workbook workbook = ExcelUtil.readWorkbook(excelFilePath)) {

      for (int i = 0; i < workbook.getNumberOfSheets(); i++) {
        var sheet = workbook.getSheetAt(i);
        String sheetName = sheet.getSheetName();

        // Iterate through all cells in the sheet using ExcelUtil
        ExcelUtil.iterateOverRowsAndCells(
            sheet,
            cell -> {
              if (cell.getCellType() == CellType.FORMULA) {
                String formula = cell.getCellFormula();

                // Check if the formula contains the string "#REF!"
                if (formula != null && formula.toUpperCase().contains("#REF!")) {
                  String cellAddress = new CellReference(cell).formatAsString();
                  errorCells.add(new RefErrorDetail(sheetName, cellAddress, formula));
                }
              }
            });
      }

    } catch (IOException e) {
      System.err.println("Error reading Excel file: " + e.getMessage());
      e.printStackTrace();
    }

    return errorCells;
  }

  static void main(String[] args) {
    Path filePath;

    // 1. Check command line argument (args[0])
    if (args.length > 0 && !args[0].isBlank()) {
      filePath = Path.of(args[0]);
    } else {
      System.out.println("Warning: No file path provided via command line arguments.");
      System.out.println("Usage: java ExcelRefErrorFinder <excel_file_path>");

      // Default file path used when args[0] is missing
      filePath = Path.of("C:/path/to/your/excel_file.xlsx");
      System.out.println("Using default path: " + filePath.toAbsolutePath() + "\n");
    }

    // 2. Validate file path
    if (!Files.exists(filePath)) {
      System.err.println("Error: File does not exist at path: " + filePath.toAbsolutePath());
      return;
    }

    if (Files.isDirectory(filePath)) {
      System.err.println("Error: Provided path is a directory, not an Excel file!");
      return;
    }

    // 3. Scan for #REF! errors
    System.out.println("Scanning for #REF! errors in file: " + filePath.getFileName() + "...\n");
    List<RefErrorDetail> errors = findRefErrorCells(filePath);

    if (errors.isEmpty()) {
      System.out.println("No cells with #REF! errors were found in formulas.");
    } else {
      System.out.println("--- FOUND " + errors.size() + " CELL(S) WITH #REF! ERRORS ---");
      errors.forEach(System.out::println);
    }
  }
}
