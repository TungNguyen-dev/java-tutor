package tungnn.tutor.java.tool.infrastructure;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import tungnn.tutor.java.tool.shared.CrawlConstant;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;

public class ImageToTextPage {

  private static final String HOME_PAGE = "https://www.imagetotext.info/";

  // Sử dụng CSS Selector linh hoạt hơn ID động
  private final By uploadInput = By.cssSelector("input[type='file']#file");
  private final By submitButton = By.id("jsShadowRoot");

  // Trang này dùng ID động dạng 'imagetotext_result...',
  // ta nên bắt theo class hoặc partial ID
  private final By resultTextArea = By.cssSelector("textarea.img-text.success_count");

  // Selector để kiểm tra trạng thái đang xử lý
  private final By processingIndicator = By.cssSelector(".loading, .processing");

  protected WebDriver driver;
  protected WebDriverWait wait;

  public ImageToTextPage(WebDriver driver) {
    this.driver = driver;
    this.wait = new WebDriverWait(driver, CrawlConstant.WAIT_TIMEOUT);
  }

  public String extractText(Path path) {
    driver.get(HOME_PAGE);

    // 1. Upload image
    if (!Files.exists(path)) {
      throw new RuntimeException("File không tồn tại: " + path);
    }

    WebElement fileInput = wait.until(ExpectedConditions.presenceOfElementLocated(uploadInput));
    fileInput.sendKeys(path.toAbsolutePath().toString());

    // 2. Submit: Đợi nút submit hiển thị và click bằng JS để tránh bị quảng cáo che
    WebElement btn = wait.until(ExpectedConditions.elementToBeClickable(submitButton));
    ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", btn);
    btn.click();

    // 3. Đợi kết quả
    // Đợi textarea xuất hiện và có chứa text (OCR xong)
    wait.until(ExpectedConditions.visibilityOfElementLocated(resultTextArea));

    // Đợi thêm một chút để đảm bảo text đã được đổ đầy vào textarea
    // (vì đôi khi element hiện ra nhưng text đang được load async)
    wait.until(
        driver ->
            !Objects.requireNonNull(driver.findElement(resultTextArea).getAttribute("value"))
                .isEmpty());

    WebElement resultElement = driver.findElement(resultTextArea);
    // Với Textarea, ta nên lấy 'value' thay vì 'getText()'
    return resultElement.getAttribute("value");
  }
}
