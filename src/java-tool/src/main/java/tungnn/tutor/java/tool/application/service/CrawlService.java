package tungnn.tutor.java.tool.application.service;

import org.openqa.selenium.WebDriver;
import tungnn.tutor.java.tool.application.dto.CrawlCourseRequest;
import tungnn.tutor.java.tool.domain.dto.CrawlRequest;
import tungnn.tutor.java.tool.domain.task.CrawlTask;
import tungnn.tutor.java.tool.infrastructure.WebDriverPool;
import tungnn.tutor.java.tool.infrastructure.WebDriverPoolFactory;
import tungnn.tutor.java.tool.shared.CrawlConstant;
import tungnn.tutor.java.tool.shared.CrawlPageEnum;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.time.Instant;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Predicate;

public class CrawlService {

  private final WebDriverPool driverPool;

  public CrawlService() {
    this.driverPool = WebDriverPoolFactory.getInstance();
  }

  public void crawlCourse(CrawlCourseRequest request) throws IOException {
    var coursePath = request.coursePath();
    var resultDir =
        CrawlConstant.STORAGE_DIR_RESULTS.resolve(String.valueOf(Instant.now().getEpochSecond()));
    var resultDirCourse = resultDir.resolve(coursePath.getFileName());
    var resultDirRequest = resultDir.resolve("request");
    var pageType = getPageType(request);
    var urls = getUrls(request);

    var counter = new AtomicInteger(1);
    var unitSize = String.valueOf(urls.size()).length();

    try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {
      for (var url : urls) {
        var unitNo = formatUnitNumber(unitSize, counter.getAndIncrement());
        var crawlRequest = new CrawlRequest(pageType, url, unitNo, resultDirCourse);
        executor.submit(() -> executeSingleCrawl(crawlRequest));
      }
    }

    Files.createDirectories(resultDirRequest);
    Files.move(
        coursePath,
        resultDirRequest.resolve(coursePath.getFileName()),
        StandardCopyOption.REPLACE_EXISTING);
  }

  private void executeSingleCrawl(CrawlRequest request) {
    WebDriver driver = null;
    try {
      // 1. Quản lý tài nguyên (Infrastructure Logic)
      driver = driverPool.borrow();

      // 2. Chuẩn bị dữ liệu và thực thi nghiệp vụ (Core Task)

      new CrawlTask(driver, request).call();

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

  private CrawlPageEnum getPageType(CrawlCourseRequest request) {
    var path = request.coursePath().toString();
    if (path.contains("01-todo-coursera")) return CrawlPageEnum.COURSERA;
    if (path.contains("01-todo-youtube")) return CrawlPageEnum.YOUTUBE;
    throw new IllegalArgumentException("Unsupported course platform in path: " + path);
  }

  private String formatUnitNumber(int size, int num) {
    return String.format("Unit %0" + size + "d", num);
  }
}
