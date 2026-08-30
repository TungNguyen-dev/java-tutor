package tungnn.tutor.java.infrastructure.pool.webdriver;

import org.openqa.selenium.WebDriver;

public interface WebDriverPool extends AutoCloseable {

  WebDriver getDriver();

  void returnDriver(WebDriver driver);
}
