package tungnn.tutor.java.selenium.crawler.core.model;

import java.nio.file.Path;
import tungnn.tutor.java.selenium.crawler.shared.PageEnum;

public record CrawlRequest(String url, PageEnum page, Path resultDir, String unitNo) {}
