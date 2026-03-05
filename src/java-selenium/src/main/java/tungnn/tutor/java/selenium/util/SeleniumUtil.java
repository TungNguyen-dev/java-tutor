package tungnn.tutor.java.selenium.util;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import tungnn.tutor.java.core.lib.io.ResourceUtil;

public final class SeleniumUtil {

  private static final String DRIVER_PATH = "chromedriver-mac-x64/chromedriver";

  private SeleniumUtil() {}

  public static WebDriver getChromeDriver() {
    System.setProperty(
        "webdriver.chrome.driver", ResourceUtil.getResourcePath(DRIVER_PATH).toString());
    return new ChromeDriver();
  }
}
