package tungnn.tutor.java.tool.crawler.core;

import org.jspecify.annotations.NonNull;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import tungnn.tutor.java.selenium.util.ElementUtil;

public class CourseraPageCrawler extends AbstractPageCrawler {

  // Locators defined as constants for easier maintenance
  private static final By VIDEO_TITLE_LOCATOR = By.cssSelector("h1.video-name");
  private static final By TRANSCRIPT_TAB_BTN_LOCATOR =
      By.cssSelector("[data-testid='item-tool-panel-button-transcript']");
  private static final By TRANSCRIPT_CONTAINER_LOCATOR = By.cssSelector("div.phrases");

  public CourseraPageCrawler(WebDriver driver) {
    super(driver);
  }

  @Override
  public boolean supports(@NonNull CrawlRequest request) {
    return request.url().contains("coursera.org");
  }

  @Override
  protected String getTitle() {
    var element =
        ElementUtil.waitUntil(
            driver, ExpectedConditions.visibilityOfElementLocated(VIDEO_TITLE_LOCATOR), timeout());
    return element.getText();
  }

  @Override
  protected String getContentAsHtml() {
    ensureTranscriptTabIsOpen();

    var transcriptContainer =
        ElementUtil.waitUntil(
            driver,
            ExpectedConditions.visibilityOfElementLocated(TRANSCRIPT_CONTAINER_LOCATOR),
            timeout());

    return ElementUtil.getAttribute(transcriptContainer, "innerHTML");
  }

  /** Checks if the transcript tab is active and opens it if necessary. */
  private void ensureTranscriptTabIsOpen() {
    var transcriptButton =
        ElementUtil.waitUntil(
            driver, ExpectedConditions.elementToBeClickable(TRANSCRIPT_TAB_BTN_LOCATOR), timeout());

    var isPressed =
        Boolean.parseBoolean(ElementUtil.getAttribute(transcriptButton, "aria-pressed"));
    if (!isPressed) {
      transcriptButton.click();

      // Wait explicitly until the transcript container becomes visible.
      ElementUtil.waitUntil(
          driver,
          ExpectedConditions.visibilityOfElementLocated(TRANSCRIPT_CONTAINER_LOCATOR),
          timeout());
    }
  }
}
