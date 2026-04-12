package tungnn.tutor.java.selenium.crawler;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Scanner;
import java.util.stream.Stream;
import tungnn.tutor.java.selenium.crawler.application.dto.CrawlCourseRequest;
import tungnn.tutor.java.selenium.crawler.application.service.CrawlService;
import tungnn.tutor.java.selenium.crawler.infra.ObsidianResultWriter;
import tungnn.tutor.java.selenium.crawler.infra.WebDriverPool;
import tungnn.tutor.java.selenium.crawler.shared.CrawlConstant;

public class Application {

  static void main(String[] args) {
    new Application().run();
  }

  public void run() {
    try (var pool =
        new WebDriverPool(CrawlConstant.CRAWLER_POOL_SIZE, CrawlConstant.STARTUP_URLS)) {

      var writer = new ObsidianResultWriter();
      var service = new CrawlService(pool, writer);

      waitForUser();

      processAllTodoDirs(service);

      System.out.println(">>> Finish all tasks.");
    } catch (Exception e) {
      System.err.println(">>> Critical Error: " + e.getMessage());
    }
  }

  private void processAllTodoDirs(CrawlService service) throws IOException {
    try (Stream<Path> dirs = Files.list(CrawlConstant.STORAGE_DIR)) {
      dirs.filter(Files::isDirectory)
          .filter(dir -> dir.getFileName().toString().startsWith("01-todo"))
          .forEach(dir -> processSingleDir(dir, service));
    }
  }

  private void processSingleDir(Path dir, CrawlService service) {
    try (Stream<Path> courses = Files.list(dir)) {
      courses
          .sorted()
          .forEach(
              coursePath -> {
                try {
                  System.out.println("Crawl: " + coursePath.getFileName());
                  service.crawlCourse(new CrawlCourseRequest(coursePath));
                } catch (IOException e) {
                  System.err.println("Failed to process course: " + coursePath);
                }
              });
    } catch (IOException e) {
      System.err.println("Failed to list directory: " + dir);
    }
  }

  private void waitForUser() {
    System.out.println(">>> Ready! Press Enter to start...");
    try (var sc = new Scanner(System.in)) {
      sc.nextLine();
    }
  }
}
