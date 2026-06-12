package tungnn.tutor.java.starter.infrastructure.excel;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import tungnn.tutor.java.office.excel.ExcelUtil;
import tungnn.tutor.java.starter.infrastructure.dictionary.Dictionary;
import tungnn.tutor.java.starter.infrastructure.dictionary.DictionaryItem;

public class ExcelTranslateUtil {

  // Precompiled patterns (performance)
  private static final Pattern NUMBER = Pattern.compile("^\\d+(\\.\\d+)?$");
  private static final Pattern DATE = Pattern.compile("^\\d{1,4}[-/]\\d{1,2}[-/]\\d{1,4}$");
  private static final Pattern CODE = Pattern.compile("^[A-Z0-9_\\-]+$");
  private static final Pattern HAS_ALPHA = Pattern.compile(".*[a-zA-Z].*");

  public static Path translateExcel(Dictionary dictionary, Path path) {
    try (var wb = ExcelUtil.readWorkbook(path)) {

      // Map Cell -> original
      Map<Cell, String> cellToOriginal = new HashMap<>();

      // 1. Scan workbook
      for (var sheet : wb) {
        for (var row : sheet) {
          for (var cell : row) {

            if (!shouldCellTranslate(cell)) {
              continue;
            }

            var rawValue = ExcelUtil.getCellContent(cell);
            if (rawValue == null) {
              continue;
            }

            var value = rawValue.toString();
            if (!shouldCellValueTranslate(value)) {
              continue;
            }

            // Delegate normalization + lookup to Dictionary
            DictionaryItem item = dictionary.getOrCreate(value);

            cellToOriginal.put(cell, item.original());
          }
        }
      }

      // 2. Translate missing entries
      dictionary.doTranslateMissing();

      // 3. Apply translations
      for (var entry : cellToOriginal.entrySet()) {
        var cell = entry.getKey();
        var original = entry.getValue();

        dictionary
            .getItemByOriginal(original)
            .map(DictionaryItem::translation)
            .ifPresent(cell::setCellValue);
      }

      // 4. Write output
      var outPath = prepareOutputPath(path);
      try (var os = Files.newOutputStream(outPath)) {
        wb.write(os);
      }

      return outPath;

    } catch (Exception e) {
      throw new RuntimeException("Failed to translate Excel file: " + path, e);
    }
  }

  private static boolean shouldCellTranslate(Cell cell) {
    if (cell == null) return false;

    var type = cell.getCellType();

    if (type == CellType.STRING) {
      return true;
    }

    return type == CellType.FORMULA && cell.getCachedFormulaResultType() == CellType.STRING;
  }

  private static boolean shouldCellValueTranslate(String value) {
    if (value == null || value.isBlank()) {
      return false;
    }

    if (NUMBER.matcher(value).matches()) {
      return false;
    }

    if (DATE.matcher(value).matches()) {
      return false;
    }

    if (CODE.matcher(value).matches()) {
      return false;
    }

    if (!HAS_ALPHA.matcher(value).matches()) {
      return false;
    }

    return true;
  }

  private static Path prepareOutputPath(Path path) {
    var filename = path.getFileName().toString();

    int dotIndex = filename.lastIndexOf('.');
    String base = (dotIndex > 0) ? filename.substring(0, dotIndex) : filename;
    String ext = (dotIndex > 0) ? filename.substring(dotIndex) : "";

    String outputName = base + "_VN" + ext;

    return path.getParent().resolve(outputName);
  }
}
