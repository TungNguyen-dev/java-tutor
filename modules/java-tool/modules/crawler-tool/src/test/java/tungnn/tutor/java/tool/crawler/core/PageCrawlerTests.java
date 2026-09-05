package tungnn.tutor.java.tool.crawler.core;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebDriver;
import tungnn.tutor.java.selenium.driver.ChromeWebDriverFactory;
import tungnn.tutor.java.selenium.driver.WebDriverFactory;
import tungnn.tutor.java.selenium.driver.options.ChromeOptionsFactory;

public class PageCrawlerTests {

  private final WebDriverFactory driverFactory =
      new ChromeWebDriverFactory(new ChromeOptionsFactory());
  private WebDriver driver;

  @AfterEach
  void tearDown() {
    if (driver != null) {
      driver.quit();
    }
  }

  @Test
  void testCrawlCourseraPage() {
    driver = driverFactory.getWebDriver("profile_0");
    PageCrawler crawler = new CourseraPageCrawler(driver);
    var url =
        "https://www.coursera.org/learn/packt-foundations-of-computer-networking-c3yh9/lecture/x1bRS/what-is-a-computer-network";

    CrawlResult result = crawler.crawl(new CrawlRequest.Page(url));

    System.out.println(result);

    // Assertions
    assertNotNull(result, "CrawlResult should not be null");
    var successResult =
        assertInstanceOf(
            CrawlResult.Success.class,
            result,
            "Expected CrawlResult.Success but got Failure: "
                + (result instanceof CrawlResult.Failure f ? f.exception().getMessage() : ""));

    assertEquals(url, successResult.url());
    assertNotNull(successResult.title(), "Page title should not be null");
    assertFalse(successResult.title().isBlank(), "Page title should not be empty");
    assertNotNull(successResult.content(), "Content should not be null");
    assertFalse(successResult.content().isBlank(), "Markdown content should not be empty");
  }

  @Test
  void testCrawlYoutubePage() {
    driver = driverFactory.getWebDriver("profile_0");
    PageCrawler crawler = new YoutubePageCrawler(driver);
    var url =
        "https://www.youtube.com/watch?v=UEAMfLPZZhE&list=PLeKd45zvjcDFUEv_ohr_HdUFe97RItdiB&index=1&pp=iAQB";

    CrawlResult result = crawler.crawl(new CrawlRequest.Page(url));

    System.out.println(result);

    // Assertions
    assertNotNull(result, "CrawlResult should not be null");
    var successResult =
        assertInstanceOf(
            CrawlResult.Success.class,
            result,
            "Expected CrawlResult.Success but got Failure: "
                + (result instanceof CrawlResult.Failure(Exception exception)
                    ? exception.getMessage()
                    : ""));

    assertEquals(url, successResult.url());
    assertNotNull(successResult.title(), "Page title should not be null");
    assertFalse(successResult.title().isBlank(), "Page title should not be empty");
    assertNotNull(successResult.content(), "Content should not be null");
    assertFalse(successResult.content().isBlank(), "Markdown content should not be empty");
  }

  @Test
  void testCrawlGenericPage() {
    driver = driverFactory.getWebDriver("profile_0");
    PageCrawler crawler = new GenericPageCrawler(driver);
    var url = "https://docs.oracle.com/javase/specs/jls/se25/html/jls-1.html";

    CrawlResult result = crawler.crawl(new CrawlRequest.Page(url));

    System.out.println(result);

    // Assertions
    assertNotNull(result, "CrawlResult should not be null");
    var successResult =
        assertInstanceOf(
            CrawlResult.Success.class,
            result,
            "Expected CrawlResult.Success but got Failure: "
                + (result instanceof CrawlResult.Failure(Exception exception)
                    ? exception.getMessage()
                    : ""));

    assertEquals(url, successResult.url());
    assertNotNull(successResult.title(), "Page title should not be null");
    assertFalse(successResult.title().isBlank(), "Page title should not be empty");
    assertNotNull(successResult.content(), "Content should not be null");
    assertFalse(successResult.content().isBlank(), "Markdown content should not be empty");
  }
}
