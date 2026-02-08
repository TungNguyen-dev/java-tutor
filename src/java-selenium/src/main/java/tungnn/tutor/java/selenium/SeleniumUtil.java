package tungnn.tutor.java.selenium;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

public final class SeleniumUtil {

  private static final String DRIVER_PATH = "storage/chromedriver-mac-x64/chromedriver";

  private SeleniumUtil() {}

  public static WebDriver getChromeDriver() {
    System.setProperty("webdriver.chrome.driver", DRIVER_PATH);
    return new ChromeDriver();
  }
}
