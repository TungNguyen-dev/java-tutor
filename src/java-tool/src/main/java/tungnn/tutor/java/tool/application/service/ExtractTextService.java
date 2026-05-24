package tungnn.tutor.java.tool.application.service;

import org.openqa.selenium.WebDriver;
import tungnn.tutor.java.tool.application.dto.ExtractCourseRequest;
import tungnn.tutor.java.tool.domain.dto.ExtractTextRequest;
import tungnn.tutor.java.tool.domain.task.ExtractTextTask;
import tungnn.tutor.java.tool.infrastructure.WebDriverPool;
import tungnn.tutor.java.tool.infrastructure.WebDriverPoolFactory;
import tungnn.tutor.java.tool.shared.ExtractTextConstant;

import java.io.IOException;
import java.nio.file.Files;
import java.time.Instant;
import java.util.concurrent.Executors;

public class ExtractTextService {

  private final WebDriverPool driverPool;

  public ExtractTextService() {
    this.driverPool = WebDriverPoolFactory.getInstance();
  }

  public void extractText(ExtractCourseRequest courseRequest) throws IOException {
    var resultDir =
        ExtractTextConstant.STORAGE_DIR_RESULTS.resolve(
            String.valueOf(Instant.now().getEpochSecond()));
    var resultDirRequest = resultDir.resolve("request");
    Files.createDirectories(resultDirRequest);

    try (var executor = Executors.newVirtualThreadPerTaskExecutor();
        var stream = Files.walk(courseRequest.todoDir())) {

      stream
          .filter(Files::isRegularFile)
          .forEach(
              p -> {
                var request = new ExtractTextRequest(p, resultDir);
                executor.submit(() -> executeSingleTask(request));
              });
    }
  }

  private void executeSingleTask(ExtractTextRequest request) {
    WebDriver driver = null;
    try {
      driver = driverPool.borrow();

      new ExtractTextTask(request, driver).call();

    } catch (Exception e) {
      throw new RuntimeException(e);
    } finally {
      if (driver != null) {
        driverPool.release(driver);
      }
    }
  }
}
