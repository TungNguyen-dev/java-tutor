package tungnn.tutor.java.selenium.crawler.core.task;

import org.openqa.selenium.WebDriver;
import tungnn.tutor.java.selenium.crawler.core.model.CrawlRequest;
import tungnn.tutor.java.selenium.crawler.infra.ObsidianResultWriter;

public class CrawlTask {

  private final WebDriver driver;
  private final CrawlRequest request;
  private final ObsidianResultWriter writer;

  public CrawlTask(WebDriver driver, CrawlRequest request, ObsidianResultWriter writer) {
    this.driver = driver;
    this.request = request;
    this.writer = writer;
  }

  public void execute() {
    System.out.printf("[%s] START - Crawling: %s%n", request.unitNo(), request.url());

    try {
      var page = PageFactory.create(driver, request.page());

      var result = page.crawl(request);

      writer.save(result);

      System.out.printf("[%s] DONE  - Saved: %s%n", request.unitNo(), result.title());

    } catch (Exception e) {
      System.err.printf(
          "[%s] ERROR - Failed to crawl %s. Reason: %s%n",
          request.unitNo(), request.url(), e.getMessage());

      throw e;
    }
  }
}
