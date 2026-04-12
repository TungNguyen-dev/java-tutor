package tungnn.tutor.java.selenium.crawler.core.page;

import java.util.stream.Collectors;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import tungnn.tutor.java.selenium.crawler.core.model.CrawlRequest;

public class CourseraPage extends BasePage {

  public CourseraPage(WebDriver driver) {
    super(driver);
  }

  @Override
  protected String getTitle(CrawlRequest request) {
    return wait.until(
            ExpectedConditions.visibilityOfElementLocated(By.cssSelector("h1.video-name")))
        .getText()
        .strip();
  }

  @Override
  protected String getContent(CrawlRequest request) {
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
