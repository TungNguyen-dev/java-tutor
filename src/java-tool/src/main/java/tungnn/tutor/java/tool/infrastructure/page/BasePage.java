package tungnn.tutor.java.tool.infrastructure.page;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import tungnn.tutor.java.tool.domain.dto.CrawlRequest;
import tungnn.tutor.java.tool.domain.dto.CrawlResult;
import tungnn.tutor.java.tool.shared.CrawlConstant;

import java.time.Duration;

public abstract class BasePage {

  protected WebDriver driver;
  protected WebDriverWait wait;

  public BasePage(WebDriver driver) {
    this.driver = driver;
    this.wait = new WebDriverWait(driver, CrawlConstant.WAIT_TIMEOUT);
  }

  public void navigateTo(String url) {
    try {
      driver.get(url);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  public CrawlResult crawl(CrawlRequest request) {
    navigateTo(request.url());

    var title = getTitle(request);
    var content = getContent(request);

    pause(Duration.ofSeconds(5));

    return new CrawlResult(request, title, content);
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
