package tungnn.tutor.java.selenium.crawler.infra;

import java.util.Set;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import org.openqa.selenium.WebDriver;
import tungnn.tutor.java.selenium.util.SeleniumUtil;

public class WebDriverPool implements AutoCloseable {

  private final BlockingQueue<WebDriver> pool;
  private final Set<String> startupUrls;
  private final int poolSize;
  private volatile boolean isClosed = false;

  public WebDriverPool(int poolSize, Set<String> startupUrls) {
    this.poolSize = poolSize;
    this.startupUrls = startupUrls;
    this.pool = new LinkedBlockingQueue<>(poolSize);
    initializePool();
  }

  private void initializePool() {
    for (int i = 0; i < poolSize; i++) {
      pool.add(createValidatedDriver());
    }
  }

  public WebDriver borrow() throws InterruptedException {
    WebDriver driver = pool.take();

    if (!isDriverAlive(driver)) {
      System.out.println("Detected dead driver, replacing with a new instance...");
      quietQuit(driver);
      return createValidatedDriver();
    }

    return driver;
  }

  public void release(WebDriver driver) {
    if (isClosed || driver == null) {
      quietQuit(driver);
      return;
    }

    if (!pool.offer(driver)) {
      // Trường hợp hy hữu pool đầy (do logic borrow/release lệch)
      quietQuit(driver);
    }
  }

  private WebDriver createValidatedDriver() {
    WebDriver driver = SeleniumUtil.createChromeDriver();
    try {
      if (startupUrls != null && !startupUrls.isEmpty()) {
        preparePrivateTabs(driver);
      }
      return driver;
    } catch (Exception e) {
      quietQuit(driver);
      throw new RuntimeException("Failed to initialize driver with private tabs", e);
    }
  }

  private void preparePrivateTabs(WebDriver driver) {
    // Duyệt qua danh sách URL cần mở sẵn (ví dụ: login pages)
    for (String url : startupUrls) {
      // Mở tab mới và truy cập URL
      SeleniumUtil.openNewTab(driver, url);
    }
    // Quay lại tab đầu tiên để sẵn sàng cho Task
    SeleniumUtil.switchToFirstTab(driver);
  }

  private boolean isDriverAlive(WebDriver driver) {
    try {
      driver.getWindowHandles(); // Lệnh kiểm tra sức khỏe driver nhanh nhất
      return true;
    } catch (Exception e) {
      return false;
    }
  }

  private void quietQuit(WebDriver driver) {
    if (driver != null) {
      try {
        driver.quit();
      } catch (Exception ignored) {
        // Ignore để không làm gián đoạn luồng chính
      }
    }
  }

  @Override
  public void close() {
    isClosed = true;
    WebDriver driver;
    while ((driver = pool.poll()) != null) {
      quietQuit(driver);
    }
  }
}
