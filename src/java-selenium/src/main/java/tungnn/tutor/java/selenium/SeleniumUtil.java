package tungnn.tutor.java.selenium;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Executors;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WindowType;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

public final class SeleniumUtil {

  private SeleniumUtil() {}

  public static WebDriver createChromeDriver() {
    return new ChromeDriver();
  }

  public static List<WebDriver> createChromeDrivers(int num) {
    var drivers = Collections.synchronizedList(new ArrayList<WebDriver>());

    try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {
      for (int i = 0; i < num; i++) {
        executor.submit(() -> drivers.add(createChromeDriver()));
      }
    }

    return new ArrayList<>(drivers);
  }

  public static WebDriver createChromeDriverWithProfile(String category, String profileName) {
    var options = new ChromeOptions();

    var cleanCategory = category.replaceAll("[^a-zA-Z0-9-_]", "_");
    var cleanProfile = profileName.replaceAll("[^a-zA-Z0-9-_]", "_");

    var profilePath =
        Path.of(System.getProperty("user.dir"), "chrome_profiles", cleanCategory, cleanProfile);

    options.addArguments("--user-data-dir=" + profilePath);
    options.addArguments("--profile-directory=Default");
    options.addArguments("--no-first-run");
    options.addArguments("--no-default-browser-check");

    return new ChromeDriver(options);
  }

  public static List<WebDriver> createChromeDriversWithProfiles(String category, int num) {
    var drivers = Collections.synchronizedList(new ArrayList<WebDriver>());

    try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {
      for (int i = 0; i < num; i++) {
        final var index = i;
        executor.submit(
            () -> {
              var specificProfileName = "Profile" + "_" + index;
              drivers.add(createChromeDriverWithProfile(category, specificProfileName));
            });
      }
    }

    return new ArrayList<>(drivers);
  }

  public static void openNewTab(WebDriver driver, String url) {
    driver.switchTo().newWindow(WindowType.TAB);
    driver.get(url);
  }

  public static void switchToFirstTab(WebDriver driver) {
    var tabs = new ArrayList<>(driver.getWindowHandles());
    if (!tabs.isEmpty()) {
      driver.switchTo().window(tabs.getFirst());
    }
  }
}
