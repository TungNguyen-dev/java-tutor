package tungnn.tutor.java.tool.crawler.page;

import java.time.Duration;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import tungnn.tutor.java.core.lib.multithread.ThreadUtil;
import tungnn.tutor.java.selenium.util.ElementUtil;

public class YoutubePage extends AbstractPage {

  private static final String VIDEO_SUMMARIZER = "https://notegpt.io/youtube-video-summarizer";

  /**
   * Constructs a new {@code AbstractPage} instance.
   *
   * @param driver the {@link WebDriver} instance, must not be null
   * @throws NullPointerException if driver is null
   */
  public YoutubePage(WebDriver driver) {
    super(driver);
  }

  @Override
  protected Duration timeout() {
    return Duration.ofSeconds(30);
  }

  @Override
  protected String homeUrl() {
    return "https://www.youtube.com";
  }

  @Override
  public String articleTitleAsHtml() {
    return extractHtmlAsString(find(By.cssSelector("#title h1 yt-formatted-string")));
  }

  @Override
  public String articleContentAsHtml() {
    var youtubeUrl = getCurrentUrl();

    navigateTo(VIDEO_SUMMARIZER);
    ThreadUtil.sleep(Duration.ofSeconds(5).toMillis());

    var locator = By.cssSelector("input[placeholder*='YouTube video link']");
    var element = find(locator);
    ElementUtil.clear(element);
    ElementUtil.sendKeys(element, youtubeUrl);

    find(By.xpath("//button[.//text()[contains(., 'Generate Summary')]]")).submit();

    waitForSummaryReady();
    return extractSummary();
  }

  private void waitForSummaryReady() {
    ElementUtil.waitUntil(
        driver,
        ExpectedConditions.elementToBeClickable(
            By.xpath(
                "//span[normalize-space()='Smart Summary']"
                    + "/ancestor::div[contains(@class,'border')]"
                    + "//span[contains(@class,'i-hugeicons:copy-01') and contains(@class,'cursor-pointer')]")),
        timeout());
  }

  private String extractSummary() {
    return ElementUtil.waitUntil(
            driver,
            ExpectedConditions.visibilityOfElementLocated(
                By.cssSelector(".md-editor.note-summary-container .md-editor-preview")),
            timeout())
        .getAttribute("innerHTML");
  }
}
