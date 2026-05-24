package tungnn.tutor.java.tool.infrastructure;

import tungnn.tutor.java.tool.shared.CrawlConstant;

public class WebDriverPoolFactory {

  private WebDriverPoolFactory() {}

  private static class Holder {
    private static final WebDriverPool INSTANCE =
        new WebDriverPool(CrawlConstant.CRAWLER_POOL_SIZE, CrawlConstant.STARTUP_URLS);
  }

  public static WebDriverPool getInstance() {
    return Holder.INSTANCE;
  }
}
