package tungnn.tutor.java.infrastructure.pool.webdriver;

import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import org.apache.commons.pool2.impl.GenericKeyedObjectPool;
import org.apache.commons.pool2.impl.GenericKeyedObjectPoolConfig;
import org.openqa.selenium.WebDriver;
import tungnn.tutor.java.selenium.driver.WebDriverFactory;

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

    // 1. Lấy profileKey đã lưu và xóa khỏi Map
    String profileKey = driverProfileMap.remove(driver);

    if (profileKey != null) {
      try {
        // Dọn dẹp session trước khi trả về pool
        driver.manage().deleteAllCookies();
      } catch (Exception ignored) {
        // Tránh throw exception nếu driver đã bị crash/đóng
      }
      // 2. Trả driver về đúng profileKey trong pool
      pool.returnObject(profileKey, driver);
    } else {
      // Trường hợp driver không thuộc pool quản lý -> ép đóng để tránh rò rỉ tài nguyên
      try {
        driver.quit();
      } catch (Exception ignored) {
      }
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
