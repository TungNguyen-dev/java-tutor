package tungnn.tutor.java.infrastructure.pool.webdriver;

import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;
import org.apache.commons.pool2.BasePooledObjectFactory;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.impl.DefaultPooledObject;
import org.openqa.selenium.WebDriver;
import tungnn.tutor.java.selenium.driver.WebDriverFactory;

public class PooledWebDriverFactory extends BasePooledObjectFactory<WebDriver> {
  private final WebDriverFactory driverFactory;
  private final BlockingQueue<String> availableProfiles;
  private final Map<WebDriver, String> driverProfileMap;

  public PooledWebDriverFactory(WebDriverFactory driverFactory, int maxTotal) {
    this.driverFactory = driverFactory;
    this.availableProfiles = new LinkedBlockingQueue<>();
    this.driverProfileMap = new ConcurrentHashMap<>();

    for (int i = 0; i < maxTotal; i++) {
      this.availableProfiles.add("profile_" + i);
    }
  }

  @Override
  public WebDriver create() throws InterruptedException {
    String profileKey = availableProfiles.take();
    try {
      WebDriver driver = driverFactory.getWebDriver(profileKey);
      driverProfileMap.put(driver, profileKey);
      return driver;
    } catch (Exception e) {
      availableProfiles.add(profileKey);
      throw e;
    }
  }

  @Override
  public PooledObject<WebDriver> wrap(WebDriver driver) {
    return new DefaultPooledObject<>(driver);
  }

  @Override
  public void destroyObject(PooledObject<WebDriver> p) {
    WebDriver driver = p.getObject();
    if (driver == null) {
      return;
    }

    String profileKey = driverProfileMap.remove(driver);
    try {
      driver.quit();
    } finally {
      if (profileKey != null) {
        availableProfiles.add(profileKey);
      }
    }
  }
}
