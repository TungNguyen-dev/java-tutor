package tungnn.tutor.java.tool.v2.infrastructure.crawler;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import tungnn.tutor.java.tool.v2.domain.task.crawl.CrawlRequest;
import tungnn.tutor.java.tool.v2.infrastructure.webdriver.WebDriverPool;

public class YoutubeSeleniumCrawlTask extends BaseSeleniumCrawlTask {

  private final String summarizePage = "https://notegpt.io/youtube-video-summarizer";

  public YoutubeSeleniumCrawlTask(WebDriverPool pool) {
    super(pool);
  }

  @Override
  protected String getTitle(WebDriver driver, WebDriverWait wait, CrawlRequest request) {
    return wait.until(
            ExpectedConditions.presenceOfElementLocated(
                By.cssSelector("#title h1 yt-formatted-string")))
        .getText()
        .strip();
  }

  @Override
  protected String getContent(WebDriver driver, WebDriverWait wait, CrawlRequest request) {
    driver.get(summarizePage);
    inputVideoUrl(wait, request.crawlUrl());
    submit(wait);
    waitForSummaryReady(wait);
    return extractSummary(wait);
  }

  private void inputVideoUrl(WebDriverWait wait, String videoUrl) {
    WebElement input =
        wait.until(
            ExpectedConditions.visibilityOfElementLocated(
                By.cssSelector("input[placeholder*='YouTube video link']")));
    input.clear();
    input.sendKeys(videoUrl);
  }

  private void submit(WebDriverWait wait) {
    wait.until(
            ExpectedConditions.elementToBeClickable(
                By.xpath("//button[.//text()[contains(., 'Generate Summary')]]")))
        .click();
  }

  private void waitForSummaryReady(WebDriverWait wait) {
    wait.until(
        ExpectedConditions.elementToBeClickable(
            By.xpath(
                "//span[normalize-space()='Smart Summary']"
                    + "/ancestor::div[contains(@class,'border')]"
                    + "//span[contains(@class,'i-hugeicons:copy-01') and contains(@class,'cursor-pointer')]")));
  }

  private String extractSummary(WebDriverWait wait) {
    return wait.until(
            ExpectedConditions.visibilityOfElementLocated(
                By.cssSelector(".md-editor.note-summary-container .md-editor-preview")))
        .getAttribute("innerHTML");
  }

  @Override
  public String getName() {
    return "YoutubeSeleniumCrawlTask";
  }
}
