package tungnn.tutor.java.tool.crawler.core;

import java.util.Map;
import java.util.Objects;

public record ContentCrawlResult(
    String url,
    String content,
    Map<String, String> metadata,
    boolean isSuccess,
    String errorMessage) {

  public ContentCrawlResult {
    Objects.requireNonNull(url, "URL cannot be null");
    metadata = (metadata != null) ? Map.copyOf(metadata) : Map.of();
  }

  // --- Factory Methods ---

  public static ContentCrawlResult success(
      String url, String content, Map<String, String> metadata) {
    return new ContentCrawlResult(url, content, metadata, true, null);
  }

  public static ContentCrawlResult failure(String url, String errorMessage) {
    return new ContentCrawlResult(url, "", Map.of(), false, errorMessage);
  }

  // --- Helper Methods ---

  public String getMeta(String key) {
    return metadata.getOrDefault(key, "");
  }

  public String title() {
    return getMeta("title");
  }
}
