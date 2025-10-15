package tungnn.tutor.java.jasper;

import java.io.InputStream;
import java.nio.file.Path;
import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;
import javax.sql.DataSource;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.util.JRSaver;

public class JasperReportApp {

  public static void main(String[] args) throws Exception {
    JasperReportApp app = new JasperReportApp();
    app.runReport(DataSourceFactory.getOracleDataSource());
  }

  /**
   * Generates a Jasper report using the provided JDBC DataSource.
   *
   * @param dataSource a configured DataSource (e.g., Oracle, MySQL, etc.)
   */
  public void runReport(DataSource dataSource) throws Exception {
    // 1. Establish connection from the provided DataSource
    try (Connection conn = dataSource.getConnection()) {

      // 2. Load JRXML report template from classpath
      String templatePath = "jrxml/employee_report.jrxml";
      InputStream templateStream = getClass().getClassLoader().getResourceAsStream(templatePath);
      if (templateStream == null) {
        throw new IllegalStateException("Report template not found: " + templatePath);
      }

      // 3. Compile JRXML into JasperReport
      JasperReport jasperReport = JasperCompileManager.compileReport(templateStream);

      // Optional: save a compiled version (.jasper)
      Path compiledPath = Path.of("storage", "employee_report.jasper");
      JRSaver.saveObject(jasperReport, compiledPath.toAbsolutePath().toString());

      // 4. Prepare report parameters (if any)
      Map<String, Object> parameters = new HashMap<>();
      parameters.put("reportTitle", "Employee Report");
      parameters.put("createdBy", "JasperReportApp");

      // 5. Fill the report using JDBC connection
      JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, conn);

      // 6. Export the filled report to PDF
      Path pdfPath = Path.of("storage", "employee_report.pdf");
      JasperExportManager.exportReportToPdfFile(jasperPrint, pdfPath.toString());
    }
  }
}
