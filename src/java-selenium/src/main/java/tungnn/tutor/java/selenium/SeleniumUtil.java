package tungnn.tutor.java.selenium;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Executors;

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

  public static WebDriver createChromeDriverWithProfile(String profile) {
    var options = new ChromeOptions();

    var profilePath = Path.of(System.getProperty("user.dir"), ".data", "chrome_profiles", profile);
    options.addArguments("--user-data-dir=" + profilePath);

    options.addArguments("--profile-directory=Default");
    options.addArguments("--no-first-run");
    options.addArguments("--no-default-browser-check");

    return new ChromeDriver(options);
  }

  public static List<WebDriver> createChromeDriversWithProfiles(int num) {
    var drivers = Collections.synchronizedList(new ArrayList<WebDriver>());

    try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {
      for (int i = 0; i < num; i++) {
        var specificProfileName = "profile" + "_" + i;
        Runnable task =
            () -> {
              drivers.add(createChromeDriverWithProfile(specificProfileName));
            };
        executor.submit(task);
      }
    }

    return new ArrayList<>(drivers);
  }
}
