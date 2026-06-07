package tungnn.tutor.java.selenium.element;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class Elements implements Iterable<Element> {

  private final WebDriver driver;
  private final WebDriverWait wait;
  private final By locator;
  private final Duration timeout;

  // Internal state holding evaluated components (used for filtered snapshots)
  private final List<WebElement> explicitElements;

  // Primary constructor for dynamic evaluation via Locator
  private Elements(WebDriver driver, By locator, Duration timeout) {
    this.driver = driver;
    this.locator = locator;
    this.timeout = timeout;
    this.wait = new WebDriverWait(driver, timeout);
    this.explicitElements = null;
  }

  // Private constructor for handling frozen/filtered snapshots safely
  private Elements(
      WebDriver driver, List<WebElement> explicitElements, WebDriverWait wait, Duration timeout) {
    this.driver = driver;
    this.locator = null;
    this.timeout = timeout;
    this.wait = wait;
    this.explicitElements = explicitElements;
  }

  // =========================================================
  // ================= FACTORY METHODS =======================
  // =========================================================

  public static Elements $$(WebDriver driver, By locator) {
    return new Elements(driver, locator, Duration.ofSeconds(10));
  }

  public static Elements $$(WebDriver driver, By locator, Duration timeout) {
    return new Elements(driver, locator, timeout);
  }

  public static Elements fromSnapshot(
      WebDriver driver, List<WebElement> elements, WebDriverWait wait, Duration timeout) {
    return new Elements(driver, elements, wait, timeout);
  }

  // =========================================================
  // ================= INTERNAL RESOLVERS ====================
  // =========================================================

  private boolean isSnapshot() {
    return explicitElements != null;
  }

  private List<WebElement> els() {
    return isSnapshot() ? explicitElements : driver.findElements(locator);
  }

  private List<WebElement> visibleEls() {
    if (isSnapshot()) {
      return wait.until(ExpectedConditions.visibilityOfAllElements(explicitElements));
    }
    return wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(locator));
  }

  // =========================================================
  // ================= BASIC OPERATIONS =======================
  // =========================================================

  public int size() {
    return els().size();
  }

  public boolean isEmpty() {
    return size() == 0;
  }

  public Element first() {
    return get(0);
  }

  public Element get(int index) {
    // Ensuring visibility before fetching an index guarantees target interactability
    List<WebElement> list = visibleEls();
    if (index < 0 || index >= list.size()) {
      throw new IndexOutOfBoundsException(
          "Cannot access index " + index + " on elements collection size: " + list.size());
    }
    return Element.wrap(driver, list.get(index), timeout);
  }

  // =========================================================
  // ================= STREAM-LIKE OPERATIONS ================
  // =========================================================

  public List<Element> asList() {
    return els().stream().map(e -> Element.wrap(driver, e, timeout)).collect(Collectors.toList());
  }

  public Elements filter(Predicate<Element> predicate) {
    // FIX: Map individual WebElements into wrapped elements correctly to test the predicate
    List<WebElement> filtered =
        els().stream()
            .filter(e -> predicate.test(Element.wrap(driver, e, timeout)))
            .collect(Collectors.toList());

    return new Elements(driver, filtered, wait, timeout);
  }

  public Optional<Element> findFirst(Predicate<Element> predicate) {
    // FIX: Accurately wraps and evaluates elements distinctly
    return els().stream().map(e -> Element.wrap(driver, e, timeout)).filter(predicate).findFirst();
  }

  public List<String> texts() {
    return visibleEls().stream().map(WebElement::getText).collect(Collectors.toList());
  }

  // =========================================================
  // ================= ACTIONS & ASSERTIONS ===================
  // =========================================================

  public Elements clickAll() {
    visibleEls().forEach(WebElement::click);
    return this;
  }

  public Elements shouldHaveSize(int expected) {
    int actual = size();
    if (actual != expected) {
      throw new AssertionError(
          "Expected size: " + expected + ", but was: " + actual + " for locator: " + locator);
    }
    return this;
  }

  public Elements shouldNotBeEmpty() {
    if (isEmpty()) {
      throw new AssertionError(
          "Elements collection should not be empty. Target: "
              + (locator != null ? locator : "Snapshot"));
    }
    return this;
  }

  // =========================================================
  // ================= WAIT STRATEGIES =======================
  // =========================================================

  public Elements waitVisible() {
    visibleEls();
    return this;
  }

  // =========================================================
  // ================= ITERABLE IMPLEMENTATION ===============
  // =========================================================

  @Override
  public Iterator<Element> iterator() {
    return asList().iterator();
  }

  @Override
  public String toString() {
    if (isSnapshot()) {
      return "Elements{Snapshot collection size=" + explicitElements.size() + "}";
    }
    return "Elements{" + locator + '}';
  }
}
