package tungnn.tutor.java.selenium.factory;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

public class ChromeWebDriverCreator implements WebDriverCreator {

  @Override
  public WebDriver createWebDriver() {
    return new ChromeDriver();
  }
}
