package tungnn.tutor.java.tool.crawler.core;

public interface ContentCrawler {

  ContentCrawlResult crawl(ContentCrawlRequest request);

  default ContentCrawlResult crawlSafe(ContentCrawlRequest request) {
    try {
      return crawl(request);
    } catch (Exception e) {
      return ContentCrawlResult.failure(
          request != null ? request.url() : "UNKNOWN_URL",
          e.getMessage() != null ? e.getMessage() : e.getClass().getSimpleName());
    }
  }

  default ContentCrawlResult crawl(String url) {
    return crawlSafe(new ContentCrawlRequest(url));
  }
}
