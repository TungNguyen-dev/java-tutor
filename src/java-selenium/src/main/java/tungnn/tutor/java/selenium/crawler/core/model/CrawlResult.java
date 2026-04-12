package tungnn.tutor.java.selenium.crawler.core.model;

import java.nio.file.Path;

public record CrawlResult(String url, String title, String content, Path resultPath) {}
