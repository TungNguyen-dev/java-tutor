package tungnn.tutor.java.tool.v2.infrastructure.extractor;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import tungnn.tutor.java.tool.v2.domain.task.extract.ExtractRequest;
import tungnn.tutor.java.tool.v2.infrastructure.webdriver.WebDriverPool;

public class SeleniumImageToTextTask extends BaseSeleniumExtractTask {

  private final By uploadInput = By.cssSelector("input[type='file']#file");
  private final By submitButton = By.id("jsShadowRoot");
  private final By resultTextArea = By.cssSelector("textarea.img-text.success_count");

  public SeleniumImageToTextTask(WebDriverPool pool) {
    super(pool, "https://www.imagetotext.info/");
  }

  @Override
  public String getName() {
    return "Image To Text OCR Task";
  }

  @Override
  protected String performExtraction(WebDriver driver, WebDriverWait wait, ExtractRequest request) {
    var path = Path.of(request.filePath());

    if (!Files.exists(path)) {
      throw new RuntimeException("File không tồn tại: " + path);
    }

    // 1. Upload
    WebElement fileInput = wait.until(ExpectedConditions.presenceOfElementLocated(uploadInput));
    fileInput.sendKeys(path.toAbsolutePath().toString());

    // 2. Submit bằng helper jsClick từ lớp base
    WebElement btn = wait.until(ExpectedConditions.elementToBeClickable(submitButton));
    jsClick(driver, btn);

    // 3. Wait & Get Result
    wait.until(ExpectedConditions.visibilityOfElementLocated(resultTextArea));
    wait.until(
        d ->
            !Objects.requireNonNull(d.findElement(resultTextArea).getAttribute("value")).isEmpty());

    return driver.findElement(resultTextArea).getAttribute("value");
  }
}
