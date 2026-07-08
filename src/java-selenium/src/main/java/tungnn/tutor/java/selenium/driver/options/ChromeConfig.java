package tungnn.tutor.java.selenium.driver.options;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import org.openqa.selenium.chrome.ChromeOptions;

public final class ChromeConfig {

  public static final String DRIVER_PROP = "webdriver.chrome.driver";
  public static final String BINARY_PROP = "webdriver.chrome.binary";

  public static final String DEFAULT_DRIVER_PATH =
      Path.of(
              System.getProperty("user.home"),
              ".data",
              "selenium",
              "chrome",
              "chromedriver-mac-x64",
              "chromedriver")
          .toString();

  public static final String DEFAULT_BINARY_PATH =
      Path.of(
              System.getProperty("user.home"),
              ".data",
              "selenium",
              "chrome",
              "chrome-mac-x64",
              "Google Chrome for Testing.app",
              "Contents",
              "MacOS",
              "Google Chrome for Testing")
          .toString();

  public static final Path PROFILE_ROOT_DIR =
      Path.of(System.getProperty("user.home"), ".data", "selenium", "chrome", "chrome-profiles");

  private ChromeConfig() {}

  public static void initializeDriverProperty() {
    var currentDriver = System.getProperty(DRIVER_PROP);
    if (currentDriver == null || currentDriver.isBlank()) {
      System.setProperty(DRIVER_PROP, DEFAULT_DRIVER_PATH);
    }
  }

  public static void configureChromeBinary(ChromeOptions options) {
    var binaryPath = System.getProperty(BINARY_PROP);
    if (binaryPath == null || binaryPath.isBlank()) {
      options.setBinary(DEFAULT_BINARY_PATH);
    } else {
      options.setBinary(binaryPath);
    }
  }

  public static void configureProfile(ChromeOptions options, String profileName) {
    if (profileName == null || profileName.isBlank()) {
      return;
    }

    var profilePath = PROFILE_ROOT_DIR.resolve(profileName);
    try {
      Files.createDirectories(profilePath);
    } catch (IOException e) {
      throw new RuntimeException("Failed to create profile directory: " + profilePath, e);
    }

    options.addArguments("--user-data-dir=" + profilePath.toAbsolutePath());
    options.addArguments("--profile-directory=Default");
  }

  public static void configureArguments(ChromeOptions options) {
    options.addArguments(
        "--no-first-run",
        "--no-default-browser-check",
        "--no-sandbox",
        "--disable-dev-shm-usage",
        "--disable-notifications");

    options.setExperimentalOption(
        "prefs", Map.of("profile.default_content_setting_values.notifications", 2));
  }
}
