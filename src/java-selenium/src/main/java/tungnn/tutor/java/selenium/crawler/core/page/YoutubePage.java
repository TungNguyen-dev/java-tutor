package tungnn.tutor.java.selenium.crawler.core.page;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import tungnn.tutor.java.selenium.crawler.core.model.CrawlRequest;

public class YoutubePage extends BasePage {

  private final String summarizePage = "";

  public YoutubePage(WebDriver driver) {
    super(driver);
  }

  @Override
  protected String getTitle(CrawlRequest request) {
    return wait.until(
            ExpectedConditions.presenceOfElementLocated(
                By.cssSelector("#title h1 yt-formatted-string")))
        .getText()
        .strip();
  }

  @Override
  protected String getContent(CrawlRequest request) {
    navigateTo(summarizePage);
    inputVideoUrl(request.url());
    submit();
    waitForSummaryReady();
    return extractSummary();
  }

  private void inputVideoUrl(String videoUrl) {
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
