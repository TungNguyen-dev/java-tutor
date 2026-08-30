package tungnn.tutor.java.infrastructure.pool.webdriver;

import org.apache.commons.pool2.BaseKeyedPooledObjectFactory;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.impl.DefaultPooledObject;
import org.openqa.selenium.WebDriver;
import tungnn.tutor.java.selenium.driver.WebDriverFactory;

public class KeyedWebDriverFactory extends BaseKeyedPooledObjectFactory<String, WebDriver> {

  private final WebDriverFactory webDriverFactory;

  public KeyedWebDriverFactory(WebDriverFactory webDriverFactory) {
    this.webDriverFactory = webDriverFactory;
  }

  @Override
  public WebDriver create(String profileKey) {
    return webDriverFactory.getWebDriver(profileKey);
  }

  @Override
  public PooledObject<WebDriver> wrap(WebDriver driver) {
    return new DefaultPooledObject<>(driver);
  }

  @Override
  public void destroyObject(String key, PooledObject<WebDriver> p) {
    WebDriver driver = p.getObject();
    if (driver != null) {
      driver.quit();
    }
  }

  @Override
  public boolean validateObject(String key, PooledObject<WebDriver> p) {
    WebDriver driver = p.getObject();
    try {
      if (driver == null) return false;
      driver.getWindowHandle();
      return true;
    } catch (Exception e) {
      return false;
    }
  }
}
