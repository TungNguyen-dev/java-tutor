package tungnn.tutor.java.selenium.driver.pool;

import org.openqa.selenium.WebDriver;

public interface WebDriverPool extends AutoCloseable {

  void init();

  WebDriver borrow();

  void release(WebDriver driver);

  void shutdown();

  int poolSize();
}
