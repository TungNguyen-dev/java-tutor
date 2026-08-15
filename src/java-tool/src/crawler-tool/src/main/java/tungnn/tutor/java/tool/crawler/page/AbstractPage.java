package tungnn.tutor.java.tool.crawler.page;

import java.time.Duration;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import tungnn.tutor.java.selenium.util.DriverUtil;
import tungnn.tutor.java.selenium.util.ElementUtil;

/**
 * Abstract base class representing a web page in the Page Object Model (POM) pattern. Provides core
 * page lifecycle management, navigation, and contextual element interaction wrappers.
 */
public abstract class AbstractPage {

  /** The WebDriver instance used for interacting with the browser. */
  protected final WebDriver driver;

  /**
   * Constructs a new {@code AbstractPage} instance.
   *
   * @param driver the {@link WebDriver} instance, must not be null
   * @throws NullPointerException if driver is null
   */
  public AbstractPage(WebDriver driver) {
    this.driver = Objects.requireNonNull(driver, "WebDriver cannot be null");
  }

  // --- Navigation & Lifecycle Methods ---

  /**
   * Navigates to the specified URL.
   *
   * @param url the target URL to navigate to
   */
  public void navigateTo(String url) {
    DriverUtil.navigateTo(driver, url);
  }

  /** Navigates to the page's default home URL defined by {@link #homeUrl()}. */
  public void navigateToHome() {
    navigateTo(homeUrl());
  }

  /**
   * Gets the title of the current page.
   *
   * @return the page title
   */
  public String getTitle() {
    return driver.getTitle();
  }

  /**
   * Gets the current URL of the active browser tab.
   *
   * @return the current URL string
   */
  public String getCurrentUrl() {
    return driver.getCurrentUrl();
  }

  /**
   * Waits until the DOM {@code readyState} equals {@code "complete"} using the page-specific
   * timeout defined by {@link #timeout()}.
   */
  public void waitForLoad() {
    waitForLoad(timeout());
  }

  /**
   * Waits until the DOM {@code readyState} equals {@code "complete"} with a custom timeout.
   *
   * @param customTimeout the maximum duration to wait for the page load condition
   */
  public void waitForLoad(Duration customTimeout) {
    ElementUtil.waitUntil(
        driver,
        webDriver ->
            "complete"
                .equals(
                    ((org.openqa.selenium.JavascriptExecutor) webDriver)
                        .executeScript("return document.readyState")),
        customTimeout);
  }

  // --- Essential Element Interaction Wrappers ---
  // Uses page-specific timeout() automatically to eliminate boilerplate in subclasses.

  /**
   * Finds a visible web element using the page's default timeout.
   *
   * @param locator the element locator
   * @return the visible {@link WebElement}
   */
  protected WebElement find(By locator) {
    return ElementUtil.findVisibleElement(driver, locator, timeout());
  }

  /**
   * Attempts to find an element if present in the DOM without throwing exceptions.
   *
   * @param locator the element locator
   * @return an {@link Optional} containing the {@link WebElement} if present
   */
  protected Optional<WebElement> findIfPresent(By locator) {
    return ElementUtil.findElementIfPresent(driver, locator);
  }

  /**
   * Finds all elements matching the specified locator.
   *
   * @param locator the element locator
   * @return a list of matching {@link WebElement} instances
   */
  protected List<WebElement> findElements(By locator) {
    return ElementUtil.findElements(driver, locator);
  }

  /**
   * Gets the visible inner text of an element found by locator.
   *
   * @param locator the element locator
   * @return the visible inner text
   */
  protected String getText(By locator) {
    return ElementUtil.getText(driver, locator);
  }

  /**
   * Clicks on an element located by the specified locator using the page's default timeout.
   *
   * @param locator the element locator
   */
  protected void click(By locator) {
    ElementUtil.click(driver, locator, timeout());
  }

  /**
   * Clicks on the specified web element directly.
   *
   * @param element the target element
   */
  protected void click(WebElement element) {
    ElementUtil.click(element);
  }

  /**
   * Types text into an element found by locator using the page's default timeout.
   *
   * @param locator the element locator
   * @param value the text string to enter
   */
  protected void type(By locator, String value) {
    ElementUtil.type(driver, locator, value);
  }

  /**
   * Checks if an element located by the specified locator is visible on the page.
   *
   * @param locator the element locator
   * @return {@code true} if displayed, {@code false} otherwise
   */
  protected boolean isDisplayed(By locator) {
    return ElementUtil.isDisplayed(driver, locator);
  }

  protected String extractHtmlAsString(WebElement element) {
    return ElementUtil.getAttribute(element, "innerHTML");
  }

  // --- Getters & Abstract Configurations ---

  /**
   * Returns the underlying WebDriver instance.
   *
   * @return the {@link WebDriver} instance
   */
  public WebDriver getDriver() {
    return driver;
  }

  /**
   * Defines the default timeout duration for actions and element lookup on this page.
   *
   * @return the timeout duration
   */
  protected abstract Duration timeout();

  /**
   * Defines the home/entry point URL for this page.
   *
   * @return the home URL string
   */
  protected abstract String homeUrl();

  /**
   * Extracts the main title of the article as an HTML string.
   *
   * @return the article title as HTML
   */
  public abstract String articleTitleAsHtml();

  /**
   * Extracts the main content/body of the article as an HTML string.
   *
   * @return the article content as HTML
   */
  public abstract String articleContentAsHtml();
}
