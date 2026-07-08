package tungnn.tutor.java.selenium;

import java.util.ArrayList;
import java.util.List;
import org.openqa.selenium.WebDriver;
import tungnn.tutor.java.selenium.driver.ChromeWebDriverFactory;
import tungnn.tutor.java.selenium.driver.options.DefaultChromeOptionsFactory;

public final class SeleniumUtil {

  private static final ChromeWebDriverFactory driverFactory =
      new ChromeWebDriverFactory(new DefaultChromeOptionsFactory());

  private SeleniumUtil() {}

  public static WebDriver createChromeDriver() {
    return driverFactory.getWebDriver();
  }

  public static List<WebDriver> createChromeDrivers(int num) {
    var drivers = new ArrayList<WebDriver>();
    for (int index = 0; index < num; index++) {
      var driver = createChromeDriver();
      drivers.add(driver);
    }
    return drivers;
  }

  public static WebDriver createChromeDriverWithProfile(String profile) {
    return driverFactory.getWebDriver(profile);
  }

  public static List<WebDriver> createChromeDriversWithProfiles(int num) {
    return driverFactory.getWebDrivers(num);
  }
}
