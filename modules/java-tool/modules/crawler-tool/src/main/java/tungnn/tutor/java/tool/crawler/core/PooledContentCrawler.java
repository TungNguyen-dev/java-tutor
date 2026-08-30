package tungnn.tutor.java.tool.crawler.core;

import java.util.Map;
import org.openqa.selenium.WebDriver;
import tungnn.tutor.java.infrastructure.pool.webdriver.WebDriverPool;
import tungnn.tutor.java.tool.crawler.page.PageType;

public class PooledContentCrawler implements ContentCrawler {

  private final WebDriverPool driverPool;

  public PooledContentCrawler(WebDriverPool driverPool) {
    this.driverPool = driverPool;
  }

  @Override
  public ContentCrawlResult crawl(ContentCrawlRequest request) {
    var url = request.url();
    WebDriver driver = null;

    try {
      // 1. Mượn driver từ pool
      driver = driverPool.getDriver();

      // 2. Thực hiện nghiệp vụ crawl
      var pageType =
          PageType.fromUrl(url)
              .orElseThrow(() -> new IllegalArgumentException("Unsupported URL: " + url));
      var page = pageType.createPageInstance(driver);

      page.navigateTo(url);
      var metadata = Map.of("title", page.articleTitleAsHtml());
      var content = page.articleContentAsHtml();

      return ContentCrawlResult.success(url, content, metadata);

    } catch (Exception e) {
      return ContentCrawlResult.failure(url, e.getMessage());

    } finally {
      // 3. Luôn đảm bảo trả lại driver về pool dù thành công hay ném ra ngoại lệ
      if (driver != null) {
        driverPool.returnDriver(driver);
      }
    }
  }
}
