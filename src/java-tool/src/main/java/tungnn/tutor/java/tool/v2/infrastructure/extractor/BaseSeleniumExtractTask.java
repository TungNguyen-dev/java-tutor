package tungnn.tutor.java.tool.v2.infrastructure.extractor;

import java.time.Duration;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import tungnn.tutor.java.tool.v2.domain.task.extract.ExtractRequest;
import tungnn.tutor.java.tool.v2.domain.task.extract.ExtractResult;
import tungnn.tutor.java.tool.v2.domain.task.extract.ExtractTask;
import tungnn.tutor.java.tool.v2.infrastructure.webdriver.WebDriverPool;

public abstract class BaseSeleniumExtractTask extends ExtractTask {

  protected final WebDriverPool pool;
  protected final String homePage;

  protected BaseSeleniumExtractTask(WebDriverPool pool, String homePage) {
    this.pool = pool;
    this.homePage = homePage;
  }

  @Override
  protected final ExtractResult doExecute(ExtractRequest request) {
    WebDriver driver = null;
    try {
      // 1. Mượn driver từ pool
      driver = pool.borrow();
      // Sử dụng timeout từ hằng số hoặc cấu hình mặc định 5 phút như BaseSeleniumCrawlTask
      WebDriverWait wait = new WebDriverWait(driver, Duration.ofMinutes(5));

      // 2. Điều hướng tới trang extract
      driver.get(homePage);

      // 3. Thực thi logic extract cụ thể
      String resultText = performExtraction(driver, wait, request);

      return new ExtractResult(resultText);
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
      throw new RuntimeException("Extract task interrupted", e);
    } finally {
      // 4. Trả driver về pool
      if (driver != null) {
        pool.release(driver);
      }
    }
  }

  /** Lớp con sẽ triển khai các bước chi tiết (Upload, Click, Wait result) */
  protected abstract String performExtraction(
      WebDriver driver, WebDriverWait wait, ExtractRequest request);

  /** Helper method để thực thi script an toàn, thừa hưởng từ logic JS Executor của bạn */
  protected void jsClick(WebDriver driver, org.openqa.selenium.WebElement element) {
    ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", element);
    element.click();
  }
}
