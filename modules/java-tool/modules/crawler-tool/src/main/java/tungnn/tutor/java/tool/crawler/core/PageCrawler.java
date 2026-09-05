package tungnn.tutor.java.tool.crawler.core;

public interface PageCrawler {

  boolean supports(CrawlRequest request);

  CrawlResult crawl(CrawlRequest crawlRequest);
}
