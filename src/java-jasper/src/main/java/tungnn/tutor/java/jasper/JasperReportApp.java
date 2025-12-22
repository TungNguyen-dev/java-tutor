package tungnn.tutor.java.jasper;

import net.sf.jasperreports.engine.*;

import javax.sql.DataSource;
import java.io.InputStream;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class JasperReportApp {

  private static final DataSource DATA_SOURCE = DataSourceFactory.getOracleDataSource();

  static void main() throws SQLException, JRException {
    // 1. Establish connection from the provided DataSource
    Connection conn = Objects.requireNonNull(DATA_SOURCE).getConnection();

    // 2. Load JRXML report template from classpath
    String templatePath = "jrxml/employee_report.jrxml";
    InputStream templateStream =
        JasperReportApp.class.getClassLoader().getResourceAsStream(templatePath);
    if (templateStream == null) {
      throw new IllegalStateException("Report template not found: " + templatePath);
    }

    // 3. Compile JRXML into JasperReport
    JasperReport jasperReport = JasperCompileManager.compileReport(templateStream);

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
