package tungnn.tutor.java.selenium.driver.pool;

import java.util.Objects;
import org.apache.commons.pool2.BaseKeyedPooledObjectFactory;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.impl.DefaultPooledObject;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import tungnn.tutor.java.selenium.driver.WebDriverFactory;

public class WebDriverPooledObjectFactory extends BaseKeyedPooledObjectFactory<String, WebDriver> {

  private final WebDriverFactory factory;

  public WebDriverPooledObjectFactory(WebDriverFactory factory) {
    this.factory = Objects.requireNonNull(factory, "factory must not be null");
  }

  @Override
  public WebDriver create(String profile) {
    WebDriver driver = factory.getWebDriver(profile);
    if (driver == null) {
      throw new IllegalStateException("WebDriverFactory returned null for profile: " + profile);
    }
    return driver;
  }

  @Override
  public PooledObject<WebDriver> wrap(WebDriver driver) {
    return new DefaultPooledObject<>(driver);
  }

  @Override
  public void destroyObject(String key, PooledObject<WebDriver> pooled) {
    if (pooled == null || pooled.getObject() == null) {
      return;
    }
    try {
      pooled.getObject().quit();
    } catch (WebDriverException ex) {
      // Best-effort cleanup: browser/session may already be gone.
      System.err.println("Failed to quit WebDriver for key '" + key + "': " + ex.getMessage());
    }
  }

  @Override
  public boolean validateObject(String key, PooledObject<WebDriver> pooled) {
    if (pooled == null || pooled.getObject() == null) {
      return false;
    }
    try {
      pooled.getObject().getWindowHandle();
      return true;
    } catch (WebDriverException ex) {
      return false;
    }
  }
}
