package tungnn.tutor.java.tool.crawler.core.infrastructure.pool;

import org.openqa.selenium.WebDriver;
import tungnn.tutor.java.spring.tool.crawler.domain.WebCrawler;
import tungnn.tutor.java.spring.tool.crawler.domain.WebCrawlerType;
import tungnn.tutor.java.spring.tool.crawler.infrastructure.crawler.CourseraWebCrawler;
import tungnn.tutor.java.spring.tool.crawler.infrastructure.crawler.GenericWebCrawler;
import tungnn.tutor.java.spring.tool.crawler.infrastructure.crawler.YoutubeWebCrawler;

public class WebCrawlerFactory {

  public static WebCrawler create(WebCrawlerType type, WebDriver driver) {
    return switch (type) {
      case COURSERA -> new CourseraWebCrawler(driver);
      case YOUTUBE -> new YoutubeWebCrawler(driver);
      case GENERIC -> new GenericWebCrawler(driver);
    };
  }
}
