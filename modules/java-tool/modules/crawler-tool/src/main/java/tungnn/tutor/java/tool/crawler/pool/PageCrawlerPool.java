package tungnn.tutor.java.tool.crawler.pool;

import tungnn.tutor.java.tool.crawler.core.PageCrawler;
import tungnn.tutor.java.tool.crawler.core.PageCrawlerType;

public interface PageCrawlerPool extends AutoCloseable {

  PageCrawler borrowPageCrawler(PageCrawlerType type);

  void returnPageCrawler(PageCrawlerType type, PageCrawler pageCrawler);
}
