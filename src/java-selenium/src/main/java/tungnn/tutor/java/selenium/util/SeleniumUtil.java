package tungnn.tutor.java.selenium.util;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.util.ArrayList;

public final class SeleniumUtil {

  private static final String DRIVER_PATH = "storage/chromedriver-mac-x64/chromedriver";

  static {
    System.setProperty("webdriver.chrome.driver", DRIVER_PATH);
  }

  private SeleniumUtil() {}

  public static WebDriver createChromeDriver() {
    ChromeOptions options = new ChromeOptions();
    options.addArguments("--disable-notifications");

    // 1. Lấy độ phân giải màn hình thực tế
    var screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
    int screenWidth = (int) screenSize.getWidth();
    int screenHeight = (int) screenSize.getHeight();

    // 2. Tính toán kích thước (1/6 màn hình)
    // Chia màn hình làm 3 cột và 2 hàng (hoặc tùy bạn chia tỉ lệ)
    int width = screenWidth / 2;
    int height = screenHeight / 2;

    // 3. Thiết lập vị trí (Ví dụ: Góc trên bên phải)
    int x = screenWidth - width;
    int y = 0;

    // 4. Thêm argument vào Chrome
    options.addArguments(String.format("--window-size=%d,%d", width, height));
    options.addArguments(String.format("--window-position=%d,%d", x, y));

    // Một số flags giúp trình duyệt chạy nhẹ hơn khi mở nhiều instance
    options.addArguments("--no-sandbox");
    options.addArguments("--disable-dev-shm-usage");

    return new ChromeDriver(options);
  }

  public static void openNewTab(WebDriver driver, String url) {
    if (driver instanceof JavascriptExecutor js) {
      js.executeScript("window.open(arguments[0], '_blank');", url);
    }
  }

  public static void switchToFirstTab(WebDriver driver) {
    var tabs = new ArrayList<>(driver.getWindowHandles());
    if (!tabs.isEmpty()) {
      driver.switchTo().window(tabs.getFirst());
    }
  }
}
