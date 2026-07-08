package tungnn.tutor.java.selenium;

import tungnn.tutor.java.selenium.driver.ChromeWebDriverFactory;
import tungnn.tutor.java.selenium.driver.options.DefaultChromeOptionsFactory;

public class ChromeWebDriverFactoryTests {

  static void main() {
    var optionsFactory = new DefaultChromeOptionsFactory();
    var driverFactory = new ChromeWebDriverFactory(optionsFactory);

    driverFactory.getWebDriver();
    driverFactory.getWebDriver("profile_0");
    driverFactory.getWebDrivers("profile_1", "profile_2");
  }
}
