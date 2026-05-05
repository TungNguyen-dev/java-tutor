package tungnn.tutor.java.selenium.util;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import java.util.ArrayList;

public final class SeleniumUtil {

  private SeleniumUtil() {}

  public static WebDriver createChromeDriver() {
    return new ChromeDriver();
  }

  public static void openNewTab(WebDriver driver, String url) {
    if (driver instanceof JavascriptExecutor js) {
      js.executeScript("window.open(arguments[0], '_blank');", url);
    }
  }

  public static void switchToFirstTab(WebDriver driver) {
    var tabs = new ArrayList<>(driver.getWindowHandles());
    if (!tabs.isEmpty()) {
      driver.switchTo().window(tabs.getFirst());
    }
  }
}
