package tungnn.tutor.java.tool.v2.infrastructure.crawler;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import tungnn.tutor.java.tool.v2.domain.task.crawl.CrawlRequest;
import tungnn.tutor.java.tool.v2.infrastructure.webdriver.WebDriverPool;

import java.time.Duration;
import java.util.stream.Collectors;

public class CourseraSeleniumCrawlTask extends BaseSeleniumCrawlTask {

  public CourseraSeleniumCrawlTask(WebDriverPool pool) {
    super(pool);
  }

  @Override
  protected String getTitle(WebDriver driver, WebDriverWait wait, CrawlRequest request) {
    return wait.until(
            ExpectedConditions.visibilityOfElementLocated(By.cssSelector("h1.video-name")))
        .getText()
        .strip();
  }

  @Override
  protected String getContent(WebDriver driver, WebDriverWait wait, CrawlRequest request) {
    wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("div.phrases")));
    return wait
        .until(webdriver -> webdriver.findElements(By.cssSelector("div.phrases span[data-cue]")))
        .stream()
        .map(WebElement::getText)
        .map(String::trim)
        .filter(text -> !text.isEmpty())
        .collect(Collectors.joining(" "));
  }

  @Override
  public String getName() {
    return "CourseraCrawlTask";
  }

  private static final Duration DEFAULT_PAUSE_DURATION = Duration.ofSeconds(15);

  @Override
  protected void pause(Duration duration) {
    super.pause(DEFAULT_PAUSE_DURATION);
  }
}
