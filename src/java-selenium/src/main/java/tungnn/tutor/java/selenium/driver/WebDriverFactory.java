package tungnn.tutor.java.selenium.driver;

import java.util.List;
import org.openqa.selenium.WebDriver;

public interface WebDriverFactory {

  WebDriver getWebDriver();

  WebDriver getWebDriver(String profileName);

  List<WebDriver> getWebDrivers(String... profileNames);

  List<WebDriver> getWebDrivers(int numberOfProfiles);
}
