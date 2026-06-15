package tungnn.tutor.java.tool.domain.dto;

import java.nio.file.Path;
import tungnn.tutor.java.tool.shared.CrawlPageEnum;

public record CrawlRequest(CrawlPageEnum page, String url, String unitNo, Path resultDir) {}
