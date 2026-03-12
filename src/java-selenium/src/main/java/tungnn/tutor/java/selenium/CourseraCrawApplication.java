package tungnn.tutor.java.selenium;

import org.jspecify.annotations.NonNull;
import tungnn.tutor.java.core.lib.io.ResourceUtil;
import tungnn.tutor.java.selenium.crawler.CourseraCrawler;
import tungnn.tutor.java.selenium.crawler.CourseraCrawlerPool;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;

public class CourseraCrawApplication {

  private static final Path STORAGE_DIR = Paths.get("storage");
  private static final Path STORAGE_DIR_NOTES = Paths.get("storage/notes");
  private static final Path STORAGE_DIR_TODO = Paths.get("storage/01_todo");
  private static final Path STORAGE_DIR_PROCESSING = Paths.get("storage/02_processing");
  private static final Path STORAGE_DIR_DONE = Paths.get("storage/03_done");
  private static final String TEMPLATE;
  private static final DateTimeFormatter ID_FORMAT =
      DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS");
  private static final int CRAWLER_POOL_SIZE = Integer.parseInt(System.getenv("CRAWLER_POOL_SIZE"));

  static {
    try {
      TEMPLATE = ResourceUtil.getResourceAsString("markdown-note-template.md");
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  static void main() throws IOException {
    try (CourseraCrawlerPool pool = new CourseraCrawlerPool(CRAWLER_POOL_SIZE);
        Scanner scanner = new Scanner(System.in)) {

      System.out.println("Press ENTER after logging in...");
      scanner.nextLine();

      boolean running = true;
      while (running) {
        List<Path> todoPathList = getTodoPathList();
        for (Path todoPath : todoPathList) {
          Path courseName = todoPath.getFileName();
          Path processPath = Files.move(todoPath, STORAGE_DIR_PROCESSING.resolve(courseName));
          Path targetDir = Files.createDirectories(STORAGE_DIR_NOTES.resolve(courseName));

          crawlCourse(pool, processPath, targetDir);

          Files.move(processPath, STORAGE_DIR_DONE.resolve(courseName));
        }

        System.out.print("Do you want to crawl again? (y/n): ");
        String input = scanner.nextLine().trim().toLowerCase();
        running = input.equals("y") || input.equals("yes");
      }

      System.out.print("Exiting crawler...");
    }
  }

  private static @NonNull List<Path> getTodoPathList() throws IOException {
    List<Path> todoPathList;
    try (Stream<Path> todoPathStream = Files.list(CourseraCrawApplication.STORAGE_DIR_TODO)) {
      todoPathList = todoPathStream.sorted().toList();
    }
    return todoPathList;
  }

  private static void crawlCourse(CourseraCrawlerPool pool, Path coursePath, Path targetDir)
      throws IOException {

    List<String> urlList = Files.readAllLines(coursePath);
    int unitNoSize = Math.max(2, urlList.size() / 10 + 1);
    AtomicInteger unitCounter = new AtomicInteger(1);

    try (ExecutorService executor = Executors.newFixedThreadPool(CRAWLER_POOL_SIZE)) {
      List<Future<?>> futures = new ArrayList<>();

      for (String url : urlList) {
        Runnable task = () -> {
          CourseraCrawler crawler = null;
          try {
            crawler = pool.borrowCrawler();

            // MAJOR: Main logic
            CourseraCrawler.CrawlResult result = crawler.craw(url);
            handleCrawlResult(
                    result,
                    unitNoSize,
                    unitCounter.getAndIncrement(),
                    targetDir
            );

          } catch (InterruptedException | IOException e) {
            throw new RuntimeException(e);
          } finally {
            pool.returnCrawler(crawler);
          }
        };

        futures.add(executor.submit(task));
      }
      for (Future<?> future : futures) {
        try {
          future.get();
        } catch (InterruptedException | ExecutionException e) {
          throw new RuntimeException(e);
        }
      }
    }
  }

  private static void handleCrawlResult(
      CourseraCrawler.CrawlResult crawlResult, int unitNoSize, int unitNo, Path targetDir)
      throws IOException {

    String id = LocalDateTime.now().format(ID_FORMAT);
    String noteName = sanitizeFileName(crawlResult.title());

    String content =
        TEMPLATE
            .replace("{{id}}", id)
            .replace("{{noteTitle}}", noteName)
            .replace("{{date}}", LocalDate.now().format(DateTimeFormatter.ISO_DATE))
            .replace("{{title}}", crawlResult.title())
            .replace("{{content}}", crawlResult.transcript())
            .replace("{{url}}", crawlResult.url());

    String noteFileName = formatUnitNumber(unitNoSize, unitNo) + " - " + noteName + ".md";
    Path targetPath = targetDir.resolve(noteFileName);

    Files.writeString(targetPath, content, StandardOpenOption.CREATE);
  }

  private static String sanitizeFileName(String input) {
    String sanitized = input.replaceAll("[^a-zA-Z0-9._-]", "_");
    return sanitized.length() > 100 ? sanitized.substring(0, 100) : sanitized;
  }

  private static String formatUnitNumber(int size, int num) {
    return String.format("Unit %0" + size + "d", num);
  }
}
