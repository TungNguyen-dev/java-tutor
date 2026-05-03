package tungnn.tutor.java.tool.v1.domain.dto;

import tungnn.tutor.java.tool.v1.shared.CrawlPageEnum;

import java.nio.file.Path;

public record CrawlRequest(CrawlPageEnum page, String url, String unitNo, Path resultDir) {}
