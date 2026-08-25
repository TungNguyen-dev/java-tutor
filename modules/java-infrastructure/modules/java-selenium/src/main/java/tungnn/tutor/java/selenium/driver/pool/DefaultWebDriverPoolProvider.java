package tungnn.tutor.java.selenium.driver.pool;

import java.time.Duration;
import org.apache.commons.pool2.impl.GenericKeyedObjectPoolConfig;
import org.openqa.selenium.WebDriver;

public class DefaultWebDriverPoolProvider implements WebDriverPoolProvider {

  @Override
  public GenericKeyedObjectPoolConfig<WebDriver> config() {
    GenericKeyedObjectPoolConfig<WebDriver> config = new GenericKeyedObjectPoolConfig<>();
    config.setMaxTotalPerKey(1);
    config.setMaxTotal(8);
    config.setMaxIdlePerKey(2);
    config.setMinIdlePerKey(1);
    config.setBlockWhenExhausted(true);
    config.setMaxWait(Duration.ofSeconds(30));
    config.setTestOnBorrow(true);
    config.setTestWhileIdle(true);
    config.setTimeBetweenEvictionRuns(Duration.ofSeconds(60));
    config.setMinEvictableIdleDuration(Duration.ofMinutes(10));
    return config;
  }
}
