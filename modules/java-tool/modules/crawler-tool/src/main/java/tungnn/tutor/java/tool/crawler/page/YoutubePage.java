package tungnn.tutor.java.tool.crawler.page;

import java.time.Duration;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import tungnn.tutor.java.selenium.util.ElementUtil;

public class YoutubePage extends AbstractPage {

  private static final String YOUTUBE_HOME_URL = "https://www.youtube.com";
  private static final String VIDEO_SUMMARIZER_URL = "https://tactiq.io/tools/youtube-transcript";
  private static final Duration DEFAULT_TIMEOUT = Duration.ofSeconds(30);

  // Locators
  private static final By TITLE_LOCATOR = By.cssSelector("#title h1 yt-formatted-string");
  private static final By FORM_LOCATOR = By.id("wf-form-YouTube-Transcript");
  private static final By INPUT_LOCATOR =
      By.cssSelector("input[type='text'], input[type='url'], input:not([type='submit'])");
  private static final By SUBMIT_BTN_LOCATOR =
      By.cssSelector("input[type='submit'], button[type='submit']");
  private static final By TRANSCRIPT_LOCATOR = By.id("transcript");
  private static final By TRANSCRIPT_ITEM_LOCATOR = By.tagName("li");
  private static final By TRANSCRIPT_LINK_LOCATOR = By.tagName("a");

  /**
   * Constructs a new {@code YoutubePage} instance.
   *
   * @param driver the {@link WebDriver} instance, must not be null
   * @throws NullPointerException if driver is null
   */
  public YoutubePage(WebDriver driver) {
    super(driver);
  }

  @Override
  protected Duration timeout() {
    return DEFAULT_TIMEOUT;
  }

  @Override
  protected String homeUrl() {
    return YOUTUBE_HOME_URL;
  }

  @Override
  public String articleTitleAsHtml() {
    return extractHtmlAsString(find(TITLE_LOCATOR));
  }

  @Override
  public String articleContentAsHtml() {
    var youtubeUrl = getCurrentUrl();

    try {
      navigateTo(VIDEO_SUMMARIZER_URL);

      var formEl =
          ElementUtil.waitUntil(
              driver, ExpectedConditions.visibilityOfElementLocated(FORM_LOCATOR), timeout());

      var inputEl = ElementUtil.findElement(formEl, INPUT_LOCATOR);
      ElementUtil.clear(inputEl);
      ElementUtil.sendKeys(inputEl, youtubeUrl);

      var submitBtn = ElementUtil.findElement(formEl, SUBMIT_BTN_LOCATOR);
      submitBtn.submit();

      return extractSummary();
    } finally {
      // Đảm bảo trình duyệt luôn quay lại URL ban đầu dù có ngoại lệ xảy ra
      navigateTo(homeUrl());
    }
  }

  private String extractSummary() {
    ElementUtil.waitUntil(
        driver,
        ExpectedConditions.visibilityOfNestedElementsLocatedBy(
            TRANSCRIPT_LOCATOR, TRANSCRIPT_ITEM_LOCATOR),
        timeout());

    var transcriptEl = find(TRANSCRIPT_LOCATOR);
    var links = ElementUtil.findChildElements(transcriptEl, TRANSCRIPT_LINK_LOCATOR);

    var sj = new java.util.StringJoiner(" ");
    for (var link : links) {
      sj.add(link.getAttribute("outerHTML"));
    }

    return sj.toString();
  }
}
