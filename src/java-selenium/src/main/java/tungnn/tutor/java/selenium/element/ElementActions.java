package tungnn.tutor.java.selenium.element;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.WebDriverWait;

public final class ElementActions {

  private ElementActions() {}

  public static void click(WebDriverWait wait, By locator) {
    WaitExecutor.execute(
        wait,
        driver -> {
          WebElement el = driver.findElement(locator);

          if (!el.isDisplayed() || !el.isEnabled()) return null;

          el.click();
          return true;
        });
  }

  public static void type(WebDriverWait wait, By locator, String text) {
    WaitExecutor.execute(
        wait,
        driver -> {
          WebElement el = driver.findElement(locator);

          if (!el.isDisplayed() || !el.isEnabled()) return null;

          // clear robust
          el.clear();
          String value = el.getAttribute("value");

          if (value != null && !value.isEmpty()) {
            el.sendKeys(Keys.chord(Keys.CONTROL, "a"));
            el.sendKeys(Keys.BACK_SPACE);
          }

          el.sendKeys(text);

          return text.equals(el.getAttribute("value")) ? true : null;
        });
  }

  public static String getText(WebDriverWait wait, By locator) {
    return WaitExecutor.execute(wait, driver -> driver.findElement(locator).getText());
  }
}
