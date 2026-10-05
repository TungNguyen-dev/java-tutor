package tungnn.tutor.java.tool.crawler.core.infrastructure.pool;

import tungnn.tutor.java.spring.tool.crawler.domain.WebCrawler;
import tungnn.tutor.java.spring.tool.crawler.domain.WebCrawlerType;

public interface WebCrawlerPool extends AutoCloseable {

  WebCrawler borrowCrawler(WebCrawlerType type);

  void returnCrawler(WebCrawlerType type, WebCrawler webCrawler);
}
