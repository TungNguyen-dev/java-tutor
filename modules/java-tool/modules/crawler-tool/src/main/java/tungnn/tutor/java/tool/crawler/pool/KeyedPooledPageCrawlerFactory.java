package tungnn.tutor.java.tool.crawler.pool;

import org.apache.commons.pool2.BaseKeyedPooledObjectFactory;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.impl.DefaultPooledObject;
import tungnn.tutor.java.selenium.pool.WebDriverPool;
import tungnn.tutor.java.tool.crawler.core.*;
import tungnn.tutor.java.tool.crawler.factory.PageCrawlerFactory;

public class KeyedPooledPageCrawlerFactory
    extends BaseKeyedPooledObjectFactory<PageCrawlerType, PageCrawler> {

  private final WebDriverPool driverPool;

  public KeyedPooledPageCrawlerFactory(WebDriverPool driverPool) {
    this.driverPool = driverPool;
  }

  @Override
  public PageCrawler create(PageCrawlerType key) throws Exception {
    var driver = driverPool.borrowDriver();
    return PageCrawlerFactory.create(key, driver);
  }

  @Override
  public PooledObject<PageCrawler> wrap(PageCrawler value) {
    return new DefaultPooledObject<>(value);
  }

  @Override
  public void destroyObject(PageCrawlerType key, PooledObject<PageCrawler> p) {
    var pageCrawler = p.getObject();
    if (pageCrawler instanceof AbstractPageCrawler abstractPageCrawler) {
      var driver = abstractPageCrawler.driver();
      if (driver != null) {
        driverPool.returnDriver(driver);
      }
    }
  }
}
