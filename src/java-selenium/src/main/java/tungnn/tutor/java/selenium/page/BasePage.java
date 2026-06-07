package tungnn.tutor.java.selenium.page;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import tungnn.tutor.java.selenium.element.Element;
import tungnn.tutor.java.selenium.element.Elements;

import java.time.Duration;

public abstract class BasePage {

  protected WebDriver driver;
  protected WebDriverWait wait;

  public BasePage(WebDriver driver) {
    this.driver = driver;
    this.wait = new WebDriverWait(driver, timeout());
  }

  protected abstract Duration timeout();

  protected abstract String homeUrl();

  // =========================================================
  // ================= ELEMENT HELPERS =======================
  // =========================================================

  protected Element $(By locator) {
    return Element.$(driver, locator, timeout()); // [cite: 4]
  }

  protected Elements $$(By locator) {
    return Elements.$$(driver, locator, timeout());
  }

  // =========================================================
  // ================= NAVIGATION HELPERS ====================
  // =========================================================

  /** Điều hướng đến một URL bất kỳ. */
  protected void navigateTo(String url) {
    driver.get(url);
  }

  /** Làm mới (Refresh) trang hiện tại. */
  protected void refreshPage() {
    driver.navigate().refresh();
  }

  /** Quay lại (Back) trang trước đó. */
  protected void back() {
    driver.navigate().back();
  }

  /** Đi tới (Forward) trang kế tiếp trong lịch sử trình duyệt. */
  protected void forward() {
    driver.navigate().forward();
  }

  /** Lấy Title của trang hiện tại. */
  protected String getPageTitle() {
    return driver.getTitle();
  }

  /** Lấy URL hiện tại của trang. */
  protected String getCurrentUrl() {
    return driver.getCurrentUrl();
  }

  // =========================================================
  // ==================== WAITS HELPERS ======================
  // =========================================================

  /** Chờ cho Title của trang chứa một đoạn text mong muốn. */
  protected boolean waitTitleContains(String titlePart) {
    return wait.until(ExpectedConditions.titleContains(titlePart));
  }

  /** Chờ cho URL của trang chứa một đoạn text mong muốn (Hữu ích khi verify chuyển trang). */
  protected boolean waitUrlContains(String urlPart) {
    return wait.until(ExpectedConditions.urlContains(urlPart));
  }

  // =========================================================
  // ================== JAVASCRIPT HELPERS ===================
  // =========================================================

  /** Thực thi một đoạn JavaScript bất kỳ. */
  protected Object executeJs(String script, Object... args) {
    return ((JavascriptExecutor) driver).executeScript(script, args);
  }

  /** Chờ cho trang và tất cả các request Ajax/Network (nếu có) được load xong hoàn toàn. */
  protected void waitPageLoadComplete() {
    wait.until(d -> executeJs("return document.readyState").equals("complete"));
  }

  // =========================================================
  // ================= WINDOWS & ALERTS ======================
  // =========================================================

  /** Chấp nhận (Accept) một cửa sổ Alert Pop-up. */
  protected void acceptAlert() {
    wait.until(ExpectedConditions.alertIsPresent()).accept();
  }

  /** Hủy (Dismiss) một cửa sổ Alert Pop-up. */
  protected void dismissAlert() {
    wait.until(ExpectedConditions.alertIsPresent()).dismiss();
  }
}
