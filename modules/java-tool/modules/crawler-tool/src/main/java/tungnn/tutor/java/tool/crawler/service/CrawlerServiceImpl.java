package tungnn.tutor.java.tool.crawler.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import tungnn.tutor.java.tool.crawler.core.CrawlRequest;
import tungnn.tutor.java.tool.crawler.manager.PageCrawlerManager;

public class CrawlerServiceImpl implements CrawlerService {

  private final PageCrawlerManager pageCrawlerManager;

  public CrawlerServiceImpl(PageCrawlerManager pageCrawlerManager) {
    this.pageCrawlerManager = pageCrawlerManager;
  }

  @Override
  public CrawlCourseResult crawlCourse(CrawlCourseRequest request) {
    if (request == null || request.courses() == null || request.courses().isEmpty()) {
      return new CrawlCourseResult(Set.of());
    }

    var courseRequestsMap =
        request.courses().stream()
            .collect(
                Collectors.toMap(
                    course -> course,
                    course ->
                        readUrlsFromFile(course.path()).stream()
                            .map(CrawlRequest.Page::new)
                            .toList()));

    var flatPageRequests = courseRequestsMap.values().stream().flatMap(List::stream).toList();

    var crawlResultMap = pageCrawlerManager.submitBatch(flatPageRequests);

    var resultCourses =
        courseRequestsMap.entrySet().stream()
            .map(
                entry -> {
                  var courseReq = entry.getKey();
                  var pageRequests = entry.getValue();

                  var crawlResults = pageRequests.stream().map(crawlResultMap::get).toList();

                  return new CrawlCourseResult.Course(courseReq.relativePath(), crawlResults);
                })
            .collect(Collectors.toSet());

    return new CrawlCourseResult(resultCourses);
  }

  private List<String> readUrlsFromFile(Path path) {
    try (var lines = Files.lines(path)) {
      return lines
          .map(String::trim)
          .filter(line -> !line.isBlank() && !line.startsWith("#"))
          .toList();
    } catch (IOException e) {
      return List.of();
    }
  }
}
