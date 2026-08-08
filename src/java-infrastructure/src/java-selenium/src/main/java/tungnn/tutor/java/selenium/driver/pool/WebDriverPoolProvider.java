package tungnn.tutor.java.selenium.driver.pool;

import java.util.Objects;
import org.apache.commons.pool2.impl.GenericKeyedObjectPool;
import org.apache.commons.pool2.impl.GenericKeyedObjectPoolConfig;
import org.openqa.selenium.WebDriver;
import tungnn.tutor.java.selenium.driver.WebDriverFactory;

public interface WebDriverPoolProvider {

  /**
   * Build the pool tuning for this strategy. Implementations override this to define local / grid /
   * latency profiles.
   */
  GenericKeyedObjectPoolConfig<WebDriver> config();

  /**
   * Assemble a ready-to-use pool. Same wiring for every strategy, so it lives here as a default
   * method.
   */
  default GenericKeyedObjectPool<String, WebDriver> create(WebDriverFactory factory) {
    Objects.requireNonNull(factory, "factory must not be null");
    WebDriverPooledObjectFactory objFactory = new WebDriverPooledObjectFactory(factory);
    return new GenericKeyedObjectPool<>(objFactory, config());
  }
}
