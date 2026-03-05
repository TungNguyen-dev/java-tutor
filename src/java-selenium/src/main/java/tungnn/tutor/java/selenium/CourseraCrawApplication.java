package tungnn.tutor.java.selenium;

import org.openqa.selenium.WebDriver;
import tungnn.tutor.java.core.lib.io.ResourceUtil;
import tungnn.tutor.java.selenium.crawler.CourseraCrawler;
import tungnn.tutor.java.selenium.util.SeleniumUtil;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicInteger;

public class CourseraCrawApplication {

  private static final String URL =
      "https://www.coursera.org/programs/fpt-software-complete-learning-program-me8hh?authProvider=fpt-software";
  private static final Path STORAGE_DIR = Paths.get("storage");
  private static final Path STORAGE_DIR_NOTES = Paths.get("storage/notes");
  private static final Path STORAGE_DIR_TODO = Paths.get("storage/01_todo");
  private static final Path STORAGE_DIR_PROCESSING = Paths.get("storage/02_processing");
  private static final Path STORAGE_DIR_DONE = Paths.get("storage/03_done");
  private static final String TEMPLATE;
  private static final DateTimeFormatter ID_FORMAT =
      DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS");

  static {
    try {
      TEMPLATE = ResourceUtil.getResourceAsString("markdown-note-template.md");
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  static void main() throws IOException {
    WebDriver driver = SeleniumUtil.getChromeDriver();

    try (Scanner scanner = new Scanner(System.in)) {

      driver.get(URL);
      System.out.println("Press ENTER after logging in...");
      scanner.nextLine();

      CourseraCrawler crawler = new CourseraCrawler(driver);

      boolean running = true;
      while (running) {
        // MAJOR: core logic
        run(crawler);

        System.out.print("Do you want to crawl again? (y/n): ");
        String input = scanner.nextLine().trim().toLowerCase();
        running = input.equals("y") || input.equals("yes");
      }

      System.out.print("Exiting crawler...");

    } finally {
      driver.quit();
    }
  }

  public static void run(CourseraCrawler crawler) throws IOException {
    // Load all file in storage/01_todo
    List<Path> todoList = Files.list(STORAGE_DIR_TODO).sorted().toList();

    // move a file to storage/02_processing
    for (Path coursePath : todoList) {
      Path processPath =
          Files.move(coursePath, STORAGE_DIR_PROCESSING.resolve(coursePath.getFileName()));

      List<String> urlList = Files.readAllLines(processPath);
      int unitNoSize = Math.max(2, urlList.size() / 10 + 1);
      AtomicInteger unitCounter = new AtomicInteger(1);
      for (String url : urlList) {
        CourseraCrawler.CrawlResult crawlResult;
        try {
          crawlResult = crawler.craw(url);
        } catch (Exception e) {
          System.out.println("Url crawl failed: " + url);
          System.out.println(e.getMessage());
          continue;
        }
        String id = LocalDateTime.now().format(ID_FORMAT);
        String noteName = sanitizeFileName(crawlResult.title());
        String content =
            TEMPLATE
                .replace("{{id}}", id)
                .replace("{{noteTitle}}", noteName)
                .replace("{{date}}", LocalDate.now().format(DateTimeFormatter.ISO_DATE))
                .replace("{{title}}", crawlResult.title())
                .replace("{{content}}", crawlResult.transcript())
                .replace("{{url}}", url);

        String noteFileName =
            formatUnitNumber(unitNoSize, unitCounter.getAndIncrement()) + " - " + noteName + ".md";
        Path notePath = STORAGE_DIR_NOTES.resolve(noteFileName);
        Files.writeString(notePath, content, StandardOpenOption.CREATE);
      }

      Files.move(processPath, STORAGE_DIR_DONE.resolve(processPath.getFileName()));
    }
  }

  private static String sanitizeFileName(String input) {
    String sanitized = input.replaceAll("[^a-zA-Z0-9._-]", "_");
    return sanitized.length() > 100 ? sanitized.substring(0, 100) : sanitized;
  }

  private static String formatUnitNumber(int size, int num) {
    return String.format("Unit %0" + size + "d", num);
  }
}
