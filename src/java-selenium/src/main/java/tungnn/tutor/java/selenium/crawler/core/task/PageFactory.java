package tungnn.tutor.java.selenium.crawler.core.task;

import org.openqa.selenium.WebDriver;
import tungnn.tutor.java.selenium.crawler.core.page.BasePage;
import tungnn.tutor.java.selenium.crawler.core.page.CourseraPage;
import tungnn.tutor.java.selenium.crawler.core.page.YoutubePage;
import tungnn.tutor.java.selenium.crawler.shared.PageEnum;

public class PageFactory {

  public static BasePage create(WebDriver driver, PageEnum type) {
    return switch (type) {
      case COURSERA -> new CourseraPage(driver);
      case YOUTUBE -> new YoutubePage(driver);
    };
  }
}
