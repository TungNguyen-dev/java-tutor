package tungnn.tutor.java.selenium.page;

import java.time.Duration;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import tungnn.tutor.java.selenium.page.crawler.PageCrawlResult;
import tungnn.tutor.java.selenium.page.crawler.PageCrawler;

public class YoutubePage extends BasePage implements PageCrawler {

  private static final String VIDEO_SUMMARIZER = "https://notegpt.io/youtube-video-summarizer";

  public YoutubePage(WebDriver driver) {
    super(driver);
  }

  @Override
  protected Duration timeout() {
    return Duration.ofSeconds(15);
  }

  @Override
  protected String homeUrl() {
    return "https://www.youtube.com";
  }

  @Override
  public PageCrawlResult crawl(String url) {
    navigateTo(url);
    return new PageCrawlResult(url, getLessonTitle(), getLessonContent());
  }

  private String getLessonTitle() {
    return wait.until(
            ExpectedConditions.presenceOfElementLocated(
                By.cssSelector("#title h1 yt-formatted-string")))
        .getText()
        .strip();
  }

  private String getLessonContent() {
    var url = driver.getCurrentUrl();

    navigateTo(VIDEO_SUMMARIZER);
    waitTitleContains("YouTube Video Summarizer");

    sendUrl(url);
    submit();
    waitForSummaryReady();

    return extractSummary();
  }

  private void sendUrl(String videoUrl) {
    WebElement input =
        wait.until(
            ExpectedConditions.visibilityOfElementLocated(
                By.cssSelector("input[placeholder*='YouTube video link']")));
    input.clear();
    input.sendKeys(videoUrl);
  }

  private void submit() {
    wait.until(
            ExpectedConditions.elementToBeClickable(
                By.xpath("//button[.//text()[contains(., 'Generate Summary')]]")))
        .click();
  }

  private void waitForSummaryReady() {
    wait.until(
        ExpectedConditions.elementToBeClickable(
            By.xpath(
                "//span[normalize-space()='Smart Summary']"
                    + "/ancestor::div[contains(@class,'border')]"
                    + "//span[contains(@class,'i-hugeicons:copy-01') and contains(@class,'cursor-pointer')]")));
  }

  private String extractSummary() {
    return wait.until(
            ExpectedConditions.visibilityOfElementLocated(
                By.cssSelector(".md-editor.note-summary-container .md-editor-preview")))
        .getAttribute("innerHTML");
  }
}
