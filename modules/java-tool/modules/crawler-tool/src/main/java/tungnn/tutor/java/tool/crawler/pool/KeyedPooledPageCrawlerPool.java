package tungnn.tutor.java.tool.crawler.pool;

import java.util.EnumMap;
import java.util.Map;
import org.apache.commons.pool2.KeyedPooledObjectFactory;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.PooledObjectFactory;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import tungnn.tutor.java.tool.crawler.core.PageCrawler;
import tungnn.tutor.java.tool.crawler.core.PageCrawlerType;

public class KeyedPooledPageCrawlerPool implements PageCrawlerPool {

  private final Map<PageCrawlerType, GenericObjectPool<PageCrawler>> pools =
      new EnumMap<>(PageCrawlerType.class);

  public KeyedPooledPageCrawlerPool(
      KeyedPooledObjectFactory<PageCrawlerType, PageCrawler> factory) {

    initKeyConfig(factory, PageCrawlerType.COURSERA, 1, 0, 0);
    initKeyConfig(factory, PageCrawlerType.YOUTUBE, 4, 2, 1);
    initKeyConfig(factory, PageCrawlerType.GENERIC, 4, 1, 0);
  }

  private void initKeyConfig(
      KeyedPooledObjectFactory<PageCrawlerType, PageCrawler> factory,
      PageCrawlerType type,
      int maxTotal,
      int maxIdle,
      int minIdle) {

    var config = new GenericObjectPoolConfig<PageCrawler>();
    config.setMaxTotal(maxTotal);
    config.setMaxIdle(maxIdle);
    config.setMinIdle(minIdle);
    config.setBlockWhenExhausted(true);

    PooledObjectFactory<PageCrawler> singleFactory =
        new PooledObjectFactory<>() {
          @Override
          public PooledObject<PageCrawler> makeObject() throws Exception {
            return factory.makeObject(type);
          }

          @Override
          public void destroyObject(PooledObject<PageCrawler> p) throws Exception {
            factory.destroyObject(type, p);
          }

          @Override
          public boolean validateObject(PooledObject<PageCrawler> p) {
            return factory.validateObject(type, p);
          }

          @Override
          public void activateObject(PooledObject<PageCrawler> p) throws Exception {
            factory.activateObject(type, p);
          }

          @Override
          public void passivateObject(PooledObject<PageCrawler> p) throws Exception {
            factory.passivateObject(type, p);
          }
        };

    pools.put(type, new GenericObjectPool<>(singleFactory, config));
  }

  @Override
  public PageCrawler borrowPageCrawler(PageCrawlerType type) {
    var pool = pools.get(type);
    if (pool == null) {
      pool = pools.get(PageCrawlerType.GENERIC);
    }
    try {
      return pool.borrowObject();
    } catch (Exception e) {
      throw new RuntimeException("Error borrowing PageCrawler from pool for type: " + type, e);
    }
  }

  @Override
  public void returnPageCrawler(PageCrawlerType type, PageCrawler pageCrawler) {
    if (pageCrawler != null) {
      var pool = pools.get(type);
      if (pool == null) {
        pool = pools.get(PageCrawlerType.GENERIC);
      }
      try {
        pool.returnObject(pageCrawler);
      } catch (Exception e) {
        throw new RuntimeException("Error returning PageCrawler to pool for type: " + type, e);
      }
    }
  }

  @Override
  public void close() {
    for (var pool : pools.values()) {
      if (!pool.isClosed()) {
        pool.close();
      }
    }
  }
}
