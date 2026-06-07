package tungnn.tutor.java.selenium.element;

import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public final class Element {

  private final WebDriver driver;
  private final WebDriverWait wait;
  private final By locator;
  private final WebElement element;

  private Element(WebDriver driver, By locator, Duration timeout) {
    this.driver = driver;
    this.locator = locator;
    this.element = null;
    this.wait = new WebDriverWait(driver, timeout);
  }

  private Element(WebDriver driver, WebElement element, Duration timeout) {
    this.driver = driver;
    this.element = element;
    this.locator = null;
    this.wait = new WebDriverWait(driver, timeout);
  }

  private boolean isWrappedElement() {
    return element != null;
  }

  // =========================================================
  // ================= FACTORY METHODS =======================
  // =========================================================

  public static Element $(WebDriver driver, By locator) {
    return new Element(driver, locator, Duration.ofSeconds(10));
  }

  public static Element $(WebDriver driver, By locator, Duration timeout) {
    return new Element(driver, locator, timeout);
  }

  public static Element wrap(WebDriver driver, WebElement element, Duration timeout) {
    return new Element(driver, element, timeout);
  }

  // =========================================================
  // ================= SCOPED FIND (CHILDREN) ================
  // =========================================================

  public Element $(By subLocator) {
    return $(subLocator, Duration.ofSeconds(10));
  }

  public Element $(By subLocator, Duration customTimeout) {
    return Element.wrap(driver, getElement().findElement(subLocator), customTimeout);
  }

  public Elements $$(By subLocator) {
    return $$(subLocator, Duration.ofSeconds(10));
  }

  public Elements $$(By subLocator, Duration customTimeout) {
    List<WebElement> subElements = getElement().findElements(subLocator);
    WebDriverWait customWait = new WebDriverWait(driver, customTimeout);
    return Elements.fromSnapshot(driver, subElements, customWait, customTimeout);
  }

  // =========================================================
  // ================= INTERNAL RESOLVERS ====================
  // =========================================================

  private WebElement getElement() {
    return isWrappedElement() ? element : driver.findElement(locator);
  }

  private WebElement getVisible() {
    return isWrappedElement()
        ? wait.until(ExpectedConditions.visibilityOf(element))
        : wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
  }

  private WebElement getClickable() {
    return isWrappedElement()
        ? wait.until(ExpectedConditions.elementToBeClickable(element))
        : wait.until(ExpectedConditions.elementToBeClickable(locator));
  }

  // =========================================================
  // ================= INTERACTIONS ===========================
  // =========================================================

  public void click() {
    getClickable().click();
  }

  public Element type(String text) {
    WebElement el = getVisible();
    el.clear();
    el.sendKeys(text);
    return this;
  }

  public Element clear() {
    getVisible().clear();
    return this;
  }

  public Element hover() {
    new Actions(driver).moveToElement(getVisible()).perform();
    return this;
  }

  public Element scrollIntoView() {
    ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", getElement());
    return this;
  }

  public Element clickJs() {
    ((JavascriptExecutor) driver).executeScript("arguments[0].click();", getElement());
    return this;
  }

  // =========================================================
  // ===================== GETTERS ============================
  // =========================================================

  public String text() {
    return getVisible().getText();
  }

  public String attribute(String name) {
    return getElement().getAttribute(name);
  }

  public List<WebElement> all() {
    if (isWrappedElement()) {
      return Collections.singletonList(element);
    }
    return driver.findElements(locator);
  }

  // =========================================================
  // ===================== STATE ==============================
  // =========================================================

  public boolean isVisible() {
    try {
      return getElement().isDisplayed();
    } catch (NoSuchElementException | StaleElementReferenceException e) {
      return false;
    }
  }

  public boolean isPresent() {
    if (isWrappedElement()) return true;
    return !driver.findElements(locator).isEmpty();
  }

  public boolean isEnabled() {
    try {
      return getElement().isEnabled();
    } catch (NoSuchElementException | StaleElementReferenceException e) {
      return false;
    }
  }

  // =========================================================
  // ===================== WAIT ===============================
  // =========================================================

  public Element waitVisible() {
    getVisible();
    return this;
  }

  public Element waitInvisible() {
    if (isWrappedElement()) {
      wait.until(ExpectedConditions.invisibilityOf(element));
    } else {
      wait.until(ExpectedConditions.invisibilityOfElementLocated(locator));
    }
    return this;
  }

  public Element waitClickable() {
    getClickable();
    return this;
  }

  // =========================================================
  // ===================== ADVANCED ===========================
  // =========================================================

  public Element retryClick(int attempts) {
    RuntimeException last = null;
    for (int i = 0; i < attempts; i++) {
      try {
        click();
        return this;
      } catch (RuntimeException e) {
        last = e;
      }
    }
    throw new RuntimeException("Retry click failed for: " + this, last);
  }

  public Element shouldBeVisible() {
    if (!isVisible()) {
      throw new AssertionError("Element not visible: " + this);
    }
    return this;
  }

  public Element shouldHaveText(String expected) {
    String actual = text();
    if (!actual.contains(expected)) {
      throw new AssertionError(
          "Expected text to contain: '" + expected + "', but was: '" + actual + "'");
    }
    return this;
  }

  // =========================================================
  // ===================== DEBUG ==============================
  // =========================================================

  @Override
  public String toString() {
    return "Element{"
        + (locator != null ? locator : Objects.requireNonNull(element).toString())
        + '}';
  }
}
