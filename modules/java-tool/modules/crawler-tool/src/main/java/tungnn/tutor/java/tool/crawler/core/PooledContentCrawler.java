package tungnn.tutor.java.tool.crawler.core;

import java.util.Map;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tungnn.tutor.java.infrastructure.pool.webdriver.WebDriverPool;
import tungnn.tutor.java.tool.crawler.page.PageType;

public class PooledContentCrawler implements ContentCrawler {

  private static final Logger LOGGER = LoggerFactory.getLogger(PooledContentCrawler.class);

  private final WebDriverPool driverPool;

  public PooledContentCrawler(WebDriverPool driverPool) {
    this.driverPool = driverPool;
  }

  @Override
  public ContentCrawlResult crawl(ContentCrawlRequest request) {
    var url = request.url();
    LOGGER.info("Starting crawl request for URL: {}", url);

    WebDriver driver = null;

    try {
      // 1. Mượn driver từ pool
      LOGGER.debug("Borrowing WebDriver from pool for URL: {}", url);
      driver = driverPool.getDriver();

      // 2. Thực hiện nghiệp vụ crawl
      var pageType =
          PageType.fromUrl(url)
              .orElseThrow(
                  () -> {
                    LOGGER.warn("Unsupported URL pattern: {}", url);
                    return new IllegalArgumentException("Unsupported URL: " + url);
                  });

      LOGGER.debug("Resolved page type: {} for URL: {}", pageType, url);
      var page = pageType.createPageInstance(driver);

      LOGGER.info("Navigating to URL: {}", url);
      page.navigateTo(url);

      LOGGER.debug("Extracting metadata and content for URL: {}", url);
      var metadata = Map.of("title", page.articleTitleAsHtml());
      var content = page.articleContentAsHtml();

      LOGGER.info("Successfully crawled content for URL: {}", url);
      return ContentCrawlResult.success(url, content, metadata);

    } catch (Exception e) {
      LOGGER.error("Failed to crawl content for URL: {}. Reason: {}", url, e.getMessage(), e);
      return ContentCrawlResult.failure(url, e.getMessage());
    } finally {
      // 3. Luôn đảm bảo trả lại driver về pool dù thành công hay ném ra ngoại lệ
      if (driver != null) {
        LOGGER.debug("Returning WebDriver to pool for URL: {}", url);
        driverPool.returnDriver(driver);
      }
    }
  }
}
