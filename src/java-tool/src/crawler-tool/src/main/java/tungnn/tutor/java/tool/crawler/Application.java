package tungnn.tutor.java.tool.crawler;

import tungnn.tutor.java.selenium.driver.ChromeWebDriverFactory;
import tungnn.tutor.java.selenium.driver.options.ChromeOptionsFactory;
import tungnn.tutor.java.tool.crawler.config.AppConfig;
import tungnn.tutor.java.tool.crawler.core.SimpleContentCrawler;
import tungnn.tutor.java.tool.crawler.service.impl.ContentCrawlerServiceImpl;

public class Application {

  static void main() {
    var appConfig = AppConfig.load();
    var driverFactory = new ChromeWebDriverFactory(new ChromeOptionsFactory());
    var driver = driverFactory.getWebDriver("profile_0");
    try {
      var contentCrawler = new SimpleContentCrawler(driver);
      var contentCrawlerService = new ContentCrawlerServiceImpl(appConfig, contentCrawler);
      contentCrawlerService.crawlContent();
    } finally {
      driver.quit();
    }
  }
}
