package tungnn.tutor.java.selenium.element;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.function.Function;

public final class WaitExecutor {

  private WaitExecutor() {}

  public static <T> T execute(WebDriverWait wait, Function<WebDriver, T> condition) {
    return wait.ignoring(StaleElementReferenceException.class)
        .ignoring(NoSuchElementException.class)
        .ignoring(ElementNotInteractableException.class)
        .ignoring(ElementClickInterceptedException.class)
        .until(condition);
  }
}
