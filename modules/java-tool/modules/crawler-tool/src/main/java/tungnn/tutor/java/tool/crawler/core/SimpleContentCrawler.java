package tungnn.tutor.java.tool.crawler.core;

import java.util.Map;
import java.util.Objects;
import org.openqa.selenium.WebDriver;
import tungnn.tutor.java.tool.crawler.page.PageType;

public class SimpleContentCrawler implements ContentCrawler {

  private final WebDriver driver;

  public SimpleContentCrawler(WebDriver driver) {
    this.driver = Objects.requireNonNull(driver, "WebDriver cannot be null");
  }

  @Override
  public synchronized ContentCrawlResult crawl(ContentCrawlRequest request) {
    var url = request.url();
    try {
      var pageType =
          PageType.fromUrl(url)
              .orElseThrow(() -> new IllegalArgumentException("Unsupported URL: " + url));
      var page = pageType.createPageInstance(driver);

      page.navigateTo(url);
      var content = page.articleContentAsHtml();
      var metadata = Map.of("title", page.articleTitleAsHtml());

      return ContentCrawlResult.success(url, content, metadata);
    } catch (Exception e) {
      return ContentCrawlResult.failure(url, e.getMessage());
    }
  }
}
