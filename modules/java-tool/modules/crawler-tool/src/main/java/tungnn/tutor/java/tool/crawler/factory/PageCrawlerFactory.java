package tungnn.tutor.java.tool.crawler.factory;

import org.openqa.selenium.WebDriver;
import tungnn.tutor.java.tool.crawler.core.*;

public class PageCrawlerFactory {

  public static PageCrawler create(PageCrawlerType type, WebDriver driver) {
    return switch (type) {
      case COURSERA -> new CourseraPageCrawler(driver);
      case YOUTUBE -> new YoutubePageCrawler(driver);
      case GENERIC -> new GenericPageCrawler(driver);
    };
  }
}
