package tungnn.tutor.java.selenium;

import java.time.Duration;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import org.openqa.selenium.WebDriver;

public class DefaultWebDriverPool implements WebDriverPool {

  private final BlockingQueue<WebDriver> pool;
  private final int maxSize;
  private final Duration borrowTimeout;

  private final AtomicBoolean initialized = new AtomicBoolean(false);
  private volatile boolean closed = false;

  public DefaultWebDriverPool(int maxSize, Duration borrowTimeout) {
    this.maxSize = maxSize;
    this.borrowTimeout = borrowTimeout;
    this.pool = new LinkedBlockingQueue<>(maxSize); // bounded
  }

  // =========================
  // INIT (pre-warm)
  // =========================
  @Override
  public void init() {
    if (!initialized.compareAndSet(false, true)) {
      return;
    }

    var drivers = SeleniumUtil.createChromeDriversWithProfiles(maxSize);

    for (var driver : drivers) {
      var offered = pool.offer(driver);
      if (!offered) {
        // pool full
        safeQuit(driver);
        break;
      }
    }
  }

  // =========================
  // BORROW
  // =========================
  @Override
  public WebDriver borrow() {
    ensureOpen();

    var driver = pool.poll();
    if (driver != null) {
      return driver;
    }

    try {
      driver = pool.poll(borrowTimeout.toMillis(), TimeUnit.MILLISECONDS);
      if (driver == null) {
        throw new RuntimeException("Timeout waiting for WebDriver");
      }
      return driver;
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
      throw new RuntimeException(e);
    }
  }

  // =========================
  // RELEASE
  // =========================
  @Override
  public void release(WebDriver driver) {
    if (driver == null) return;

    if (closed) {
      safeQuit(driver);
      return;
    }

    if (!isHealthy(driver)) {
      safeQuit(driver);
      return;
    }

    reset(driver);

    var offered = pool.offer(driver);
    if (!offered) {
      // pool đã đầy → không nên giữ lại
      safeQuit(driver);
    }
  }

  // =========================
  // LIFECYCLE
  // =========================
  @Override
  public void shutdown() {
    closed = true;

    WebDriver driver;
    while ((driver = pool.poll()) != null) {
      safeQuit(driver);
    }
  }

  @Override
  public void close() {
    shutdown();
  }

  // =========================
  // INTERNAL
  // =========================
  private void ensureOpen() {
    if (closed) {
      throw new IllegalStateException("Pool already closed");
    }
  }

  private void safeQuit(WebDriver driver) {
    try {
      driver.quit();
    } catch (Exception ignored) {
    }
  }

  private boolean isHealthy(WebDriver driver) {
    try {
      driver.getTitle();
      return true;
    } catch (Exception e) {
      return false;
    }
  }

  private void reset(WebDriver driver) {
    try {
      driver.manage().deleteAllCookies();
      driver.get("about:blank");
    } catch (Exception ignored) {
    }
  }
}
