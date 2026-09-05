package tungnn.tutor.java.tool.crawler.core;

public sealed interface CrawlRequest {

  String url();

  record Page(String url) implements CrawlRequest {}
}
