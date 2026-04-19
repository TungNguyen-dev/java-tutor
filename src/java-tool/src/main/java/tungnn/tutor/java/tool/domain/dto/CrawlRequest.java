package tungnn.tutor.java.tool.domain.dto;

import tungnn.tutor.java.tool.shared.CrawlPageEnum;

import java.nio.file.Path;

public record CrawlRequest(CrawlPageEnum page, String url, String unitNo, Path resultDir) {}
