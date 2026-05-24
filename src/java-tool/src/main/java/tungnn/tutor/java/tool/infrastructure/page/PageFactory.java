package tungnn.tutor.java.tool.infrastructure.page;

import org.openqa.selenium.WebDriver;
import tungnn.tutor.java.tool.shared.CrawlPageEnum;

public class PageFactory {

  public static BasePage create(WebDriver driver, CrawlPageEnum type) {
    return switch (type) {
      case COURSERA -> new CourseraPage(driver);
      case YOUTUBE -> new YoutubePage(driver);
    };
  }
}
