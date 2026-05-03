package tungnn.tutor.java.tool.v2.application.cli;

import picocli.CommandLine;
import tungnn.tutor.java.tool.v2.application.usecase.RunTaskMultiInputUseCase;
import tungnn.tutor.java.tool.v2.domain.task.crawl.CrawlRequest;
import tungnn.tutor.java.tool.v2.domain.task.crawl.CrawlTask;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Predicate;
import java.util.stream.Stream;

@CommandLine.Command(name = "crawl", description = "Bắt đầu tiến trình cào dữ liệu v2")
public class CrawlCommand implements Runnable {

  private static final Path STORAGE_DIR = Path.of("storage", "crawler");
  private static final Path STORAGE_DIR_TODO = STORAGE_DIR.resolve("01-todo");
  private static final Path STORAGE_DIR_DONE = STORAGE_DIR.resolve("02-done");

  private final RunTaskMultiInputUseCase runTaskMultiInputUseCase;
  private final CrawlTask crawlTaskCoursera;
  private final CrawlTask crawlTaskYoutube;

  public CrawlCommand(
      RunTaskMultiInputUseCase runTaskMultiInputUseCase,
      CrawlTask crawlTaskCoursera,
      CrawlTask crawlTaskYoutube) {
    this.runTaskMultiInputUseCase = runTaskMultiInputUseCase;
    this.crawlTaskCoursera = crawlTaskCoursera;
    this.crawlTaskYoutube = crawlTaskYoutube;
  }

  @Override
  public void run() {
    try {
      if (Files.notExists(STORAGE_DIR_TODO)) {
        System.err.println("Thu muc todo không ton tai: " + STORAGE_DIR_TODO);
        return;
      }

      waitForUserCrawlCoursera();

      try (Stream<Path> dirs = Files.walk(STORAGE_DIR_TODO)) {
        dirs.filter(Files::isRegularFile).sorted().forEach(this::processCourseFile);
      }
    } catch (Exception e) {
      System.err.println("Loi thuc thi crawl: " + e.getMessage());
    }
  }

  private void processCourseFile(Path coursePath) {
    try {
      System.out.println(">>> Processing Course: " + coursePath.getFileName());
      List<String> urls = getUrls(coursePath);
      if (urls.isEmpty()) {
        System.out.println("File trong hoac khong co URL hop le: " + coursePath);
        return;
      }

      int unitSize = String.valueOf(urls.size()).length();
      AtomicInteger counter = new AtomicInteger(1);

      List<CrawlRequest> requests =
          urls.stream()
              .map(
                  url ->
                      new CrawlRequest(url, formatUnitNumber(unitSize, counter.getAndIncrement())))
              .toList();

      CrawlTask selectedTask = determineTask(coursePath);
      runTaskMultiInputUseCase.execute(selectedTask, requests);

      markAsDone(coursePath);
    } catch (Exception e) {
      System.err.println("Loi khi xu ly file " + coursePath.getFileName() + ": " + e.getMessage());
    }
  }

  private CrawlTask determineTask(Path path) {
    String pathStr = path.toString().toLowerCase();
    if (pathStr.contains("coursera")) return crawlTaskCoursera;
    if (pathStr.contains("youtube")) return crawlTaskYoutube;
    throw new IllegalArgumentException("Khong xac dinh duoc loai task cho duong dan: " + path);
  }

  private void markAsDone(Path coursePath) throws IOException {
    if (Files.notExists(STORAGE_DIR_DONE)) {
      Files.createDirectories(STORAGE_DIR_DONE);
    }
    Files.move(
        coursePath,
        STORAGE_DIR_DONE.resolve(coursePath.getFileName()),
        StandardCopyOption.REPLACE_EXISTING);
    System.out.println(">>> Moved to DONE: " + coursePath.getFileName());
  }

  private List<String> getUrls(Path coursePath) throws IOException {
    try (Stream<String> lines = Files.lines(coursePath)) {
      return lines
          .filter(Predicate.not(String::isBlank))
          .map(String::trim)
          .filter(l -> l.startsWith("http"))
          .toList();
    }
  }

  private String formatUnitNumber(int size, int num) {
    return String.format("Unit %0" + size + "d", num);
  }

  private void waitForUserCrawlCoursera() throws IOException {
    if (Files.list(STORAGE_DIR_TODO.resolve("01-coursera")).anyMatch(Files::isRegularFile)) {
      System.out.println(">>> Press Enter to start...");
      Scanner sc = new Scanner(System.in);
      if (sc.hasNextLine()) {
        sc.nextLine();
      }
    }
  }
}
