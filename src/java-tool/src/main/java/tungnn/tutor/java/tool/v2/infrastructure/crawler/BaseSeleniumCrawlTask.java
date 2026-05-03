package tungnn.tutor.java.tool.v2.infrastructure.crawler;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import tungnn.tutor.java.tool.v2.domain.task.crawl.CrawlRequest;
import tungnn.tutor.java.tool.v2.domain.task.crawl.CrawlResult;
import tungnn.tutor.java.tool.v2.domain.task.crawl.CrawlTask;
import tungnn.tutor.java.tool.v2.infrastructure.webdriver.WebDriverPool;

import java.time.Duration;

public abstract class BaseSeleniumCrawlTask extends CrawlTask {

  protected final WebDriverPool pool;

  protected BaseSeleniumCrawlTask(WebDriverPool pool) {
    this.pool = pool;
  }

  @Override
  protected final CrawlResult doExecute(CrawlRequest request) {
    WebDriver driver = null;
    try {
      // 1. Mượn driver từ pool
      driver = pool.borrow();
      WebDriverWait wait = new WebDriverWait(driver, Duration.ofMinutes(5));

      // 2. Thực thi điều hướng
      driver.get(request.crawlUrl());

      // 3. Extract dữ liệu qua các hàm abstract
      String title = getTitle(driver, wait, request);
      String content = getContent(driver, wait, request);

      pause(Duration.ofSeconds(5));

      return new CrawlResult(title, content);
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
      throw new RuntimeException("Crawl task interrupted", e);
    } finally {
      // 4. Trả driver về pool an toàn
      if (driver != null) {
        pool.release(driver);
      }
    }
  }

  // Truyền trực tiếp driver và wait vào lớp con
  protected abstract String getTitle(WebDriver driver, WebDriverWait wait, CrawlRequest request);

  protected abstract String getContent(WebDriver driver, WebDriverWait wait, CrawlRequest request);

  protected void pause(Duration duration) {
    try {
      Thread.sleep(duration.toMillis());
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
    }
  }
}
