package tungnn.tutor.java.apache.poi;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;

public class WriteXlsxFileExample {

  static void main() throws IOException {
    try (Workbook workbook = new XSSFWorkbook();
        OutputStream out = Files.newOutputStream(Path.of("storage", "data-output.xlsx"))) {

      Sheet sheet = workbook.createSheet("Report");

      Row header = sheet.createRow(0);
      header.createCell(0).setCellValue("ID");
      header.createCell(1).setCellValue("Name");

      Row dataRow = sheet.createRow(1);
      dataRow.createCell(0).setCellValue(1);
      dataRow.createCell(1).setCellValue("John Doe");

      workbook.write(out);
    }
  }
}
