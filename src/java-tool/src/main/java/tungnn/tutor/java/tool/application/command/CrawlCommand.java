package tungnn.tutor.java.tool.application.command;

import picocli.CommandLine;
import tungnn.tutor.java.tool.application.dto.CrawlCourseRequest;
import tungnn.tutor.java.tool.application.service.CrawlService;
import tungnn.tutor.java.tool.infrastructure.WebDriverPoolFactory;
import tungnn.tutor.java.tool.shared.CrawlConstant;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Scanner;
import java.util.stream.Stream;

@CommandLine.Command(name = "crawl", description = "Bắt đầu tiến trình cào dữ liệu")
public class CrawlCommand implements Runnable {

  @Override
  public void run() {
    var pool = WebDriverPoolFactory.getInstance();
    try (pool) {
      var crawlService = new CrawlService();

      waitForUser();

      processAllTodoDirs(crawlService);

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
