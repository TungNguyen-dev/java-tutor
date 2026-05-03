package tungnn.tutor.java.tool.v1.domain.task;

import org.openqa.selenium.WebDriver;
import tungnn.tutor.java.tool.v1.domain.dto.CrawlRequest;
import tungnn.tutor.java.tool.v1.domain.dto.CrawlResult;
import tungnn.tutor.java.tool.v1.infrastructure.ObsidianWriter;
import tungnn.tutor.java.tool.v1.infrastructure.ObsidianWriterFactory;
import tungnn.tutor.java.tool.v1.infrastructure.dto.ObsidianWriteRequest;
import tungnn.tutor.java.tool.v1.infrastructure.page.PageFactory;

public class CrawlTask extends BaseTask<CrawlResult> {

  private final WebDriver driver;
  private final CrawlRequest request;
  private final ObsidianWriter writer;

  public CrawlTask(WebDriver driver, CrawlRequest request) {
    super("CrawlTask-" + request.url());
    this.driver = driver;
    this.request = request;
    this.writer = ObsidianWriterFactory.getInstance();
  }

  public CrawlResult execute() {
    var page = PageFactory.create(driver, request.page());

    var result = page.crawl(request);

    var obsidianRequest =
        new ObsidianWriteRequest(
            result.title(), result.content(), request.url(), request.unitNo(), request.resultDir());
    writer.save(obsidianRequest);

    System.out.printf("[%s] DONE  - Saved: %s%n", request.unitNo(), result.title());

    return result;
  }
}
