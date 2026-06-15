package tungnn.tutor.java.selenium;

import org.openqa.selenium.WebDriver;

public interface WebDriverPool extends AutoCloseable {

  void init(); // pre-create drivers

  WebDriver borrow();

  void release(WebDriver driver);

  void shutdown();
}
