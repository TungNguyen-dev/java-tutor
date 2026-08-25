package tungnn.tutor.java.tool.crawler.page;

import java.time.Duration;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import tungnn.tutor.java.core.lib.multithread.ThreadUtil;
import tungnn.tutor.java.selenium.util.ElementUtil;

public class CourseraPage extends AbstractPage {

  /**
   * Constructs a new BasePage instance.
   *
   * @param driver the {@link WebDriver} instance, must not be null
   * @throws NullPointerException if driver is null
   */
  public CourseraPage(WebDriver driver) {
    super(driver);
  }

  @Override
  protected Duration timeout() {
    return Duration.ofSeconds(30);
  }

  @Override
  protected String homeUrl() {
    return "https://www.coursera.org";
  }

  @Override
  public String articleTitleAsHtml() {
    return extractHtmlAsString(find(By.cssSelector("h1.video-name")));
  }

  @Override
  public String articleContentAsHtml() {
    openTranscriptTab();

    return extractHtmlAsString(find(By.cssSelector("div.phrases")));
  }

  private void openTranscriptTab() {
    var buttonTranscriptLocator =
        By.cssSelector("[data-testid='item-tool-panel-button-transcript']");

    var buttonTranscript =
        ElementUtil.waitUntil(
            driver, ExpectedConditions.elementToBeClickable(buttonTranscriptLocator), timeout());

    var attr = ElementUtil.getAttribute(buttonTranscript, "aria-pressed");
    if (!"true".equalsIgnoreCase(attr)) {
      buttonTranscript.click();
      ThreadUtil.sleep(1000);
    }
  }
}
