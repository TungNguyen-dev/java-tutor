package tungnn.tutor.java.tool.v1.infrastructure.page;

import org.openqa.selenium.WebDriver;
import tungnn.tutor.java.tool.v1.shared.CrawlPageEnum;

public class PageFactory {

  public static BasePage create(WebDriver driver, CrawlPageEnum type) {
    return switch (type) {
      case COURSERA -> new CourseraPage(driver);
      case YOUTUBE -> new YoutubePage(driver);
    };
  }
}
