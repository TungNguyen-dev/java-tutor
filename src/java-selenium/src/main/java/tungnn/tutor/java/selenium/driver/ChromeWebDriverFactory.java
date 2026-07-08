package tungnn.tutor.java.selenium.driver;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import tungnn.tutor.java.selenium.driver.options.ChromeOptionsFactory;

public class ChromeWebDriverFactory implements WebDriverFactory {

  private final ChromeOptionsFactory optionsFactory;

  public ChromeWebDriverFactory(ChromeOptionsFactory optionsFactory) {
    this.optionsFactory = optionsFactory;
  }

  @Override
  public WebDriver getWebDriver() {
    var options = optionsFactory.createChromeOptions();
    return new ChromeDriver(options);
  }

  @Override
  public WebDriver getWebDriver(String profileName) {
    var options = optionsFactory.createChromeOptions(profileName);
    return new ChromeDriver(options);
  }

  @Override
  public List<WebDriver> getWebDrivers(String... profileNames) {
    if (profileNames == null || profileNames.length == 0) {
      return List.of();
    }

    try (ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor()) {
      return Arrays.stream(profileNames)
          .map(profile -> CompletableFuture.supplyAsync(() -> getWebDriver(profile), executor))
          .toList()
          .stream()
          .map(CompletableFuture::join)
          .collect(Collectors.toList());
    }
  }

  @Override
  public List<WebDriver> getWebDrivers(int numberOfProfiles) {
    var prefix = "profile_";
    var profileNames = new String[numberOfProfiles];
    for (int i = 0; i < numberOfProfiles; i++) {
      profileNames[i] = prefix + i;
    }
    return getWebDrivers(profileNames);
  }
}
