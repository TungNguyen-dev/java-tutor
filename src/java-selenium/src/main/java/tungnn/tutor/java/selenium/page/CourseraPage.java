package tungnn.tutor.java.selenium.page;

import java.time.Duration;
import java.util.stream.Collectors;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import tungnn.tutor.java.selenium.page.crawler.PageCrawlResult;
import tungnn.tutor.java.selenium.page.crawler.PageCrawler;

public class CourseraPage extends BasePage implements PageCrawler {

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
  public PageCrawlResult crawl(String url) {
    navigateTo(url);
    return new PageCrawlResult(url, getLessonTitle(), getLessonContent());
  }

  private String getLessonTitle() {
    return wait.until(
            ExpectedConditions.visibilityOfElementLocated(By.cssSelector("h1.video-name")))
        .getText()
        .strip();
  }

  private String getLessonContent() {
    wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("div.phrases")));
    return wait
        .until(driver -> driver.findElements(By.cssSelector("div.phrases span[data-cue]")))
        .stream()
        .map(WebElement::getText)
        .map(String::trim)
        .filter(text -> !text.isEmpty())
        .collect(Collectors.joining(" "));
  }
}
