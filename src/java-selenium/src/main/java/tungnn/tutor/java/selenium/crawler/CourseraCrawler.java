package tungnn.tutor.java.selenium.crawler;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class CourseraCrawler {

  private static final Duration TIMEOUT = Duration.ofMinutes(5);

  private final WebDriver driver;
  private final WebDriverWait wait;

  public CourseraCrawler(WebDriver driver) {
    this.driver = driver;
    this.wait = new WebDriverWait(driver, TIMEOUT);
  }

  public record CrawlResult(String title, String transcript) {}

  public CrawlResult craw(String url) {
    driver.get(url);
    String title = extractTitle();
    String transcript = extractTranscript();
    return new CrawlResult(title, transcript);
  }

  private String extractTitle() {
    By locator = By.cssSelector("h1.video-name");
    WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    return Objects.requireNonNull(element).getText().trim();
  }

  private String extractTranscript() {
    By phrasesLocator = By.cssSelector("div.phrases");
    wait.until(ExpectedConditions.visibilityOfElementLocated(phrasesLocator));
    List<WebElement> spans =
        wait.until(driver -> driver.findElements(By.cssSelector("div.phrases span[data-cue]")));
    return spans.stream()
        .map(WebElement::getText)
        .map(String::trim)
        .filter(text -> !text.isEmpty())
        .collect(Collectors.joining(" "));
  }
}
