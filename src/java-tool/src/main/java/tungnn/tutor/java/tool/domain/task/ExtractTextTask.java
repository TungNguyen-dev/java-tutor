package tungnn.tutor.java.tool.domain.task;

import org.openqa.selenium.WebDriver;
import tungnn.tutor.java.tool.domain.dto.ExtractTextRequest;
import tungnn.tutor.java.tool.domain.dto.ExtractTextResult;
import tungnn.tutor.java.tool.infrastructure.ImageToTextPage;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.nio.file.StandardOpenOption;
import java.util.UUID;

public class ExtractTextTask extends BaseTask<ExtractTextResult> {

  private final ExtractTextRequest request;
  private final WebDriver driver;

  public ExtractTextTask(ExtractTextRequest request, WebDriver driver) {
    super("ExtractTextTask-" + UUID.randomUUID());
    this.request = request;
    this.driver = driver;
  }

  @Override
  protected ExtractTextResult execute() {
    var page = new ImageToTextPage(driver);
    var content = page.extractText(request.imagePath());

    var result = new ExtractTextResult(taskId, content, request);

    writeResult(result);
    moveImageToResult(result);

    return result;
  }

  @Override
  protected void cleanup() {
    // Giải phóng tài nguyên ảnh hoặc xóa file tạm nếu cần
    logger.debug("Hoàn tất dọn dẹp cho tác vụ OCR: {}", taskId);
  }

  private void writeResult(ExtractTextResult result) {
    var filename = result.request().imagePath().getFileName().toString() + ".md";
    try {
      Files.writeString(
          result.request().resultDir().resolve(filename),
          result.content(),
          StandardOpenOption.CREATE,
          StandardOpenOption.TRUNCATE_EXISTING);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  private void moveImageToResult(ExtractTextResult result) {
    var filename = result.request().imagePath().getFileName().toString();
    var outputPath = result.request().resultDir().resolve("request").resolve(filename);
    try {
      Files.createDirectories(outputPath.getParent());
      Files.move(request.imagePath(), outputPath, StandardCopyOption.REPLACE_EXISTING);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }
}
