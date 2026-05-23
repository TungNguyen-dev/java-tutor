package tungnn.tutor.java.selenium.element;

import java.util.List;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public final class ElementFinder {

    private ElementFinder() {}

    public static WebElement first(WebDriverWait wait, By locator) {
        List<WebElement> elements =
                wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(locator));

        if (elements.isEmpty()) {
            throw new RuntimeException("No element found: " + locator);
        }

        return elements.get(0);
    }

    public static WebElement last(WebDriverWait wait, By locator) {
        List<WebElement> elements =
                wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(locator));

        if (elements.isEmpty()) {
            throw new RuntimeException("No element found: " + locator);
        }

        return elements.get(elements.size() - 1);
    }
}