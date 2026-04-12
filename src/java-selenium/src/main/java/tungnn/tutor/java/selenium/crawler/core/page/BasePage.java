package tungnn.tutor.java.selenium.crawler.core.page;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import tungnn.tutor.java.selenium.crawler.core.model.CrawlRequest;
import tungnn.tutor.java.selenium.crawler.core.model.CrawlResult;
import tungnn.tutor.java.selenium.crawler.shared.CrawlConstant;

import java.nio.file.Path;
import java.time.Duration;

public abstract class BasePage {

  protected WebDriver driver;
  protected WebDriverWait wait;

  public BasePage(WebDriver driver) {
    this.driver = driver;
    this.wait = new WebDriverWait(driver, CrawlConstant.WAIT_TIMEOUT);
  }

  private static Path getResultPath(CrawlRequest request, String title) {
    var filename = request.unitNo() + " - " + sanitizeFileName(title) + ".md";
    return request.resultDir().resolve(filename);
  }

  private static String sanitizeFileName(String input) {
    String sanitized = input.replaceAll("[^a-zA-Z0-9._-]", "_");
    return sanitized.length() > 100 ? sanitized.substring(0, 100) : sanitized;
  }

  public void navigateTo(String url) {
    driver.get(url);
  }

  public CrawlResult crawl(CrawlRequest request) {
    navigateTo(request.url());

    var title = getTitle(request);
    var content = getContent(request);
    var resultPath = getResultPath(request, title);

    pause(Duration.ofSeconds(5));

    return new CrawlResult(request.url(), title, content, resultPath);
  }

  protected String getTitle(CrawlRequest request) {
    return null;
  }

  protected String getContent(CrawlRequest request) {
    return null;
  }

  protected void pause(Duration duration) {
    try {
      Thread.sleep(duration.toMillis());
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
    }
  }
}
