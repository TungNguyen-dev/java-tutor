package tungnn.tutor.java.selenium.driver.options;

import org.openqa.selenium.chrome.ChromeOptions;

public class DefaultChromeOptionsFactory implements ChromeOptionsFactory {

  @Override
  public ChromeOptions createChromeOptions(String profileName) {
    ChromeConfig.initializeDriverProperty();

    var options = new ChromeOptions();
    ChromeConfig.configureChromeBinary(options);
    ChromeConfig.configureArguments(options);

    if (profileName != null) {
      ChromeConfig.configureProfile(options, profileName);
    }

    return options;
  }

  @Override
  public ChromeOptions createChromeOptions() {
    return createChromeOptions(null);
  }
}
