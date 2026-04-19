package tungnn.tutor.java.tool.application.command;

import picocli.CommandLine;
import tungnn.tutor.java.tool.application.dto.ExtractCourseRequest;
import tungnn.tutor.java.tool.application.service.ExtractTextService;
import tungnn.tutor.java.tool.infrastructure.WebDriverPoolFactory;
import tungnn.tutor.java.tool.shared.ExtractTextConstant;

@CommandLine.Command(name = "extract", description = "Trích xuất dữ liệu từ file local")
public class ExtractTextCommand implements Runnable {

  @Override
  public void run() {
    var pool = WebDriverPoolFactory.getInstance();
    try (pool) {
      var extractTextService = new ExtractTextService();

      var request = new ExtractCourseRequest(ExtractTextConstant.STORAGE_DIR_TODO);
      extractTextService.extractText(request);

      System.out.println(">>> Finish all tasks.");
    } catch (Exception e) {
      System.err.println(">>> Critical Error: " + e.getMessage());
    }
  }
}
