package tungnn.tutor.java.tool.crawler.core;

import java.time.Duration;
import java.util.Objects;
import org.openqa.selenium.WebDriver;
import tungnn.tutor.java.document.markdown.MarkdownWriterUtils;
import tungnn.tutor.java.selenium.util.DriverUtil;
import tungnn.tutor.java.selenium.util.ElementUtil;
import tungnn.tutor.java.selenium.util.WindowUtil;

public abstract class AbstractPageCrawler implements PageCrawler {

  private static final System.Logger LOGGER = System.getLogger(AbstractPageCrawler.class.getName());

  protected final WebDriver driver;

  public AbstractPageCrawler(WebDriver driver) {
    this.driver = driver;
  }

  @Override
  public CrawlResult crawl(CrawlRequest request) {
    var url = request.url();
    try {
      DriverUtil.navigateTo(driver, url);
      waitForPageToLoad();

      var title = getTitle();
      var content = MarkdownWriterUtils.convertHtmlToMarkdown(getContentAsHtml());

      LOGGER.log(System.Logger.Level.INFO, "Success to crawl URL: " + url);
      return new CrawlResult.Success(url, title, content);
    } catch (Exception e) {
      LOGGER.log(System.Logger.Level.ERROR, "Failed to crawl URL: " + url, e);
      return new CrawlResult.Failure(e);
    }
  }

  protected Duration timeout() {
    return Duration.ofMinutes(5);
  }

  protected abstract String getTitle();

  protected abstract String getContentAsHtml();

  public WebDriver driver() {
    return driver;
  }

  private void waitForPageToLoad() {
    ElementUtil.newWait(driver, timeout())
        .until(
            driver ->
                Objects.equals(
                    WindowUtil.executeScript(driver, "return document.readyState"), "complete"));
  }
}
