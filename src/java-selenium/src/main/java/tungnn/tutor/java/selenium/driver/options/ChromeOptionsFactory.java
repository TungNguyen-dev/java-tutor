package tungnn.tutor.java.selenium.driver.options;

import org.openqa.selenium.chrome.ChromeOptions;

public interface ChromeOptionsFactory {

  ChromeOptions createChromeOptions(String profile);

  ChromeOptions createChromeOptions();

  ChromeOptions createChromeOptionsWithBiDiEnabled(String profileName);
}
