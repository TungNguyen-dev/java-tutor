package tungnn.tutor.java.selenium.page;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import tungnn.tutor.java.selenium.page.crawler.PageCrawlResult;
import tungnn.tutor.java.selenium.page.crawler.PageCrawler;

import java.time.Duration;

public class AwsDocSdkPage extends BasePage implements PageCrawler {

  public AwsDocSdkPage(WebDriver driver) {
    super(driver);
  }

  @Override
  protected Duration timeout() {
    return Duration.ofSeconds(30);
  }

  @Override
  protected String homeUrl() {
    return "https://docs.aws.amazon.com/sdk-for-java/latest/developer-guide/home.html";
  }

  @Override
  public PageCrawlResult crawl(String url) {
    navigateTo(url);
    return new PageCrawlResult(url, getTitle(), getContent());
  }

  private String getTitle() {
    return $(By.id("skip-link")).text();
  }

  private String getContent() {
    return $(By.id("main-col-body")).text();
  }
}
