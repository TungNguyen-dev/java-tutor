package tungnn.tutor.java.tool.crawler.core;

public record ContentCrawlRequest(String url) {

  public ContentCrawlRequest {
    if (url == null || url.isBlank()) {
      throw new IllegalArgumentException("URL cannot be null or blank");
    }
  }
}
