package tungnn.tutor.java.infrastructure.pool.webdriver;

import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import org.apache.commons.pool2.impl.GenericKeyedObjectPool;
import org.apache.commons.pool2.impl.GenericKeyedObjectPoolConfig;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import tungnn.tutor.java.selenium.driver.WebDriverFactory;
import tungnn.tutor.java.selenium.driver.options.ChromeOptionUtil;

public class KeyedWebDriverPool implements WebDriverPool {

  private final GenericKeyedObjectPool<String, WebDriver> pool;
  private final AtomicInteger profileCounter = new AtomicInteger(0);

  // Lưu ánh xạ WebDriver -> profileKey để tra cứu O(1) khi trả về pool
  private final Map<WebDriver, String> driverProfileMap = new ConcurrentHashMap<>();

  public KeyedWebDriverPool(WebDriverFactory driverFactory, int maxTotal, int maxTotalPerKey) {
    KeyedWebDriverFactory factory = new KeyedWebDriverFactory(driverFactory);

    GenericKeyedObjectPoolConfig<WebDriver> config = new GenericKeyedObjectPoolConfig<>();
    config.setMaxTotal(maxTotal);
    config.setMaxTotalPerKey(maxTotalPerKey);
    config.setMaxIdlePerKey(maxTotalPerKey);
    config.setTestOnBorrow(true);
    config.setBlockWhenExhausted(true);
    config.setMaxWait(Duration.ofSeconds(10));

    this.pool = new GenericKeyedObjectPool<>(factory, config);
  }

  @Override
  public WebDriver getDriver() {
    try {
      // 1. Tự động sinh tên profile tăng dần: profile_0, profile_1,...
      String profileKey = "profile_" + profileCounter.getAndIncrement();

      // 2. Mượn driver từ Keyed Pool
      WebDriver driver = pool.borrowObject(profileKey);
      ChromeOptionUtil.removeWebdriverAttribute((ChromeDriver) driver);

      // 3. Ghi nhớ profileKey của driver này
      driverProfileMap.put(driver, profileKey);

      return driver;
    } catch (Exception e) {
      throw new RuntimeException("Không thể lấy WebDriver từ KeyedPool", e);
    }
  }

  @Override
  public void returnDriver(WebDriver driver) {
    if (driver == null) {
      return;
    }

    // Lấy profileKey tương ứng và xóa khỏi Map
    String profileKey = driverProfileMap.remove(driver);
    returnDriver(profileKey, driver);
  }

  /** Overload hỗ trợ trả driver khi biết trước profileKey hoặc gọi từ returnDriver(driver) */
  public void returnDriver(String profileKey, WebDriver driver) {
    if (driver == null) {
      return;
    }

    // Dọn dẹp map nếu gọi trực tiếp hàm overload này
    if (profileKey == null) {
      profileKey = driverProfileMap.remove(driver);
    } else {
      driverProfileMap.remove(driver);
    }

    if (profileKey != null) {
      try {
        // Trả driver về pool theo đúng profileKey
        pool.returnObject(profileKey, driver);
      } catch (Exception e) {
        // Nếu trả lại pool lỗi (ví dụ driver bị chết/unresponsive), hủy bỏ object để tránh rò rỉ
        try {
          pool.invalidateObject(profileKey, driver);
        } catch (Exception ignored) {
          safelyQuitDriver(driver);
        }
      }
    } else {
      // Driver không do pool này quản lý
      safelyQuitDriver(driver);
    }
  }

  private void safelyQuitDriver(WebDriver driver) {
    try {
      driver.quit();
    } catch (Exception ignored) {
    }
  }

  @Override
  public void close() {
    if (!pool.isClosed()) {
      driverProfileMap.clear();
      pool.close();
    }
  }
}
