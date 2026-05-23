package tungnn.tutor.java.selenium.element;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;

public class Element {

  private final WebDriverWait wait;
  private final By locator;

  private Element(WebDriverWait wait, By locator) {
    this.wait = wait;
    this.locator = locator;
  }

  // Factory method
  public static Element $(WebDriverWait wait, By locator) {
    return new Element(wait, locator);
  }

  // ===== Actions =====

  public Element click() {
    ElementActions.click(wait, locator);
    return this;
  }

  public Element type(String text) {
    ElementActions.type(wait, locator, text);
    return this;
  }

  public String text() {
    return ElementActions.getText(wait, locator);
  }

  // ===== Finder =====

  public WebElement first() {
    return ElementFinder.first(wait, locator);
  }

  public WebElement last() {
    return ElementFinder.last(wait, locator);
  }
}
