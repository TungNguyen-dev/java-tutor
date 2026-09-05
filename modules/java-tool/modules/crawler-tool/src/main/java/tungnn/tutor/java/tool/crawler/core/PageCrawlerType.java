package tungnn.tutor.java.tool.crawler.core;

public enum PageCrawlerType {
  COURSERA,
  YOUTUBE,
  GENERIC;

  /** Resolves the PageCrawlerType based on the request URL. */
  public static PageCrawlerType resolveType(CrawlRequest request) {
    var url = request.url();
    if (url == null) {
      return PageCrawlerType.GENERIC;
    }
    if (url.contains("coursera.org")) {
      return PageCrawlerType.COURSERA;
    }
    if (url.contains("youtube.com") || url.contains("youtu.be")) {
      return PageCrawlerType.YOUTUBE;
    }
    return PageCrawlerType.GENERIC;
  }
}
