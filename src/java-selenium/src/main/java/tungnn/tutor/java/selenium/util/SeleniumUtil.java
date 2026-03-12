package tungnn.tutor.java.selenium.util;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

public final class SeleniumUtil {

  private static final String DRIVER_PATH = "storage/chromedriver-mac-x64/chromedriver";

  static {
    System.setProperty("webdriver.chrome.driver", DRIVER_PATH);
  }

  private SeleniumUtil() {}

  public static WebDriver createChromeDriver() {
    return new ChromeDriver();
  }
}
