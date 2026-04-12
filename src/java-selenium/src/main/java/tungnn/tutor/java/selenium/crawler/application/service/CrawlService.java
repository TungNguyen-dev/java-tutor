package tungnn.tutor.java.selenium.crawler.application.service;

import java.io.IOException;
import java.nio.file.Files;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Predicate;
import org.openqa.selenium.WebDriver;
import tungnn.tutor.java.selenium.crawler.application.dto.CrawlCourseRequest;
import tungnn.tutor.java.selenium.crawler.core.model.CrawlRequest;
import tungnn.tutor.java.selenium.crawler.core.task.CrawlTask;
import tungnn.tutor.java.selenium.crawler.infra.ObsidianResultWriter;
import tungnn.tutor.java.selenium.crawler.infra.WebDriverPool;
import tungnn.tutor.java.selenium.crawler.shared.CrawlConstant;
import tungnn.tutor.java.selenium.crawler.shared.PageEnum;

public class CrawlService {

  private final WebDriverPool driverPool;
  private final ObsidianResultWriter writer;

  public CrawlService(WebDriverPool driverPool, ObsidianResultWriter writer) {
    this.driverPool = driverPool;
    this.writer = writer;
  }

  public void crawlCourse(CrawlCourseRequest request) throws IOException {
    var resultDir = CrawlConstant.STORAGE_DIR_RESULTS.resolve(request.coursePath().getFileName());
    var pageType = getPageType(request);
    var urls = getUrls(request);

    var counter = new AtomicInteger(1);
    var unitSize = String.valueOf(urls.size()).length();

    try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {
      for (var url : urls) {
        var unitNo = formatUnitNumber(unitSize, counter.getAndIncrement());
        var crawlRequest = new CrawlRequest(url, pageType, resultDir, unitNo);
        executor.submit(() -> executeSingleCrawl(crawlRequest));
      }
    }
  }

  private void executeSingleCrawl(CrawlRequest request) {
    WebDriver driver = null;
    try {
      // 1. Quản lý tài nguyên (Infrastructure Logic)
      driver = driverPool.borrow();

      // 2. Chuẩn bị dữ liệu và thực thi nghiệp vụ (Core Task)

      new CrawlTask(driver, request, writer).execute();

    } catch (Exception e) {
      System.err.printf("Error crawling URL [%s]: %s%n", request.url(), e.getMessage());
    } finally {
      // 3. Đảm bảo trả lại tài nguyên trong mọi trường hợp
      if (driver != null) {
        driverPool.release(driver);
      }
    }
  }

  private List<String> getUrls(CrawlCourseRequest request) throws IOException {
    try (var stream = Files.lines(request.coursePath())) {
      return stream
          .filter(Predicate.not(String::isBlank))
          .filter(l -> l.startsWith("http"))
          .toList();
    }
  }

  private PageEnum getPageType(CrawlCourseRequest request) {
    var path = request.coursePath().toString();
    if (path.contains("01-todo-coursera")) return PageEnum.COURSERA;
    if (path.contains("01-todo-youtube")) return PageEnum.YOUTUBE;
    throw new IllegalArgumentException("Unsupported course platform in path: " + path);
  }

  private String formatUnitNumber(int size, int num) {
    return String.format("Unit %0" + size + "d", num);
  }
}
