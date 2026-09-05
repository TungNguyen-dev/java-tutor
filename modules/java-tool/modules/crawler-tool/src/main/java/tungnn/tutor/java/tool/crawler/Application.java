package tungnn.tutor.java.tool.crawler;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.stream.Collectors;
import tungnn.tutor.java.mime.FileMimeUtil;
import tungnn.tutor.java.selenium.driver.ChromeWebDriverFactory;
import tungnn.tutor.java.selenium.driver.options.ChromeOptionsFactory;
import tungnn.tutor.java.selenium.pool.PooledWebDriverPool;
import tungnn.tutor.java.tool.crawler.manager.PageCrawlerManager;
import tungnn.tutor.java.tool.crawler.pool.KeyedPooledPageCrawlerFactory;
import tungnn.tutor.java.tool.crawler.pool.KeyedPooledPageCrawlerPool;
import tungnn.tutor.java.tool.crawler.pool.PageCrawlerPool;
import tungnn.tutor.java.tool.crawler.service.CrawlCourseRequest;
import tungnn.tutor.java.tool.crawler.service.CrawlCourseResult;
import tungnn.tutor.java.tool.crawler.service.CrawlerServiceImpl;
import tungnn.tutor.java.tool.crawler.service.ObsidianServiceImpl;

public class Application {

  private static final System.Logger LOGGER = System.getLogger(Application.class.getName());

  public static void main(String[] args) {
    // 1. Load application configuration
    LOGGER.log(System.Logger.Level.INFO, "Loading application configuration...");
    var appConfig = AppConfig.load();
    ensureDirectoriesExist(appConfig);

    // 2. Scan input files (.txt) containing URL lists
    var inputFiles = scanInputFiles(appConfig.inputDir());
    if (inputFiles.isEmpty()) {
      LOGGER.log(
          System.Logger.Level.INFO,
          "No URL list files found in input directory: {0}",
          appConfig.inputDir());
      return;
    }

    LOGGER.log(
        System.Logger.Level.INFO, "Found {0} course list file(s) to crawl.", inputFiles.size());

    // 3. Initialize Infrastructure & Object Pools
    var chromeOptionsFactory = new ChromeOptionsFactory();
    var webDriverFactory = new ChromeWebDriverFactory(chromeOptionsFactory);

    try (var webDriverPool = new PooledWebDriverPool(webDriverFactory, appConfig.poolSize());
        PageCrawlerPool pageCrawlerPool =
            new KeyedPooledPageCrawlerPool(new KeyedPooledPageCrawlerFactory(webDriverPool))) {

      // 4. Initialize Application Services
      var pageCrawlerManager = new PageCrawlerManager(pageCrawlerPool);
      var crawlerService = new CrawlerServiceImpl(pageCrawlerManager);
      var obsidianService = new ObsidianServiceImpl(appConfig);

      // 5. Prepare Requests
      var courses =
          inputFiles.stream()
              .map(
                  filePath -> {
                    var relativePath = appConfig.inputDir().relativize(filePath);
                    return new CrawlCourseRequest.Course(filePath, relativePath);
                  })
              .collect(Collectors.toSet());

      var request = new CrawlCourseRequest(courses);

      // 6. Execute Crawling Process
      LOGGER.log(System.Logger.Level.INFO, "Starting data crawling process...");
      var result = crawlerService.crawlCourse(request);

      // 7. Save notes to Obsidian Vault
      LOGGER.log(System.Logger.Level.INFO, "Saving markdown notes to Obsidian vault...");
      obsidianService.saveAllCourses(result);

      // 8. Move successfully processed input files to the 'done' directory
      moveSuccessfulCourseFilesToDone(result, appConfig.inputDir(), appConfig.doneDir());

      LOGGER.log(System.Logger.Level.INFO, "Crawling and storage process completed successfully!");

    } catch (Exception e) {
      LOGGER.log(
          System.Logger.Level.ERROR,
          "Fatal error during Application execution: " + e.getMessage(),
          e);
    }
  }

  /** Ensures that input, output, and done directories exist. */
  private static void ensureDirectoriesExist(AppConfig config) {
    try {
      Files.createDirectories(config.inputDir());
      Files.createDirectories(config.outputDir());
      Files.createDirectories(config.doneDir());
    } catch (IOException e) {
      throw new RuntimeException("Failed to create storage directories", e);
    }
  }

  /** Scans all .txt files in the input directory. */
  private static List<Path> scanInputFiles(Path inputDir) {
    try (var stream = Files.walk(inputDir)) {
      return stream
          .filter(Files::isRegularFile)
          .filter(p -> ".txt".equals(FileMimeUtil.getExtension(FileMimeUtil.getMimeType(p))))
          .toList();
    } catch (IOException e) {
      LOGGER.log(System.Logger.Level.ERROR, "Error scanning input directory: " + e.getMessage(), e);
      return List.of();
    }
  }

  /**
   * Filters successful courses (`course.success()`) and moves the corresponding input files to
   * doneDir.
   */
  private static void moveSuccessfulCourseFilesToDone(
      CrawlCourseResult result, Path inputDir, Path doneDir) {
    if (result == null || result.courses() == null) {
      return;
    }

    for (var course : result.courses()) {
      if (!course.success()) {
        LOGGER.log(
            System.Logger.Level.WARNING,
            "Course failed or contains failed lessons, retaining input file: {0}",
            course.relativePath());
        continue;
      }

      var relativePath = course.relativePath();
      var sourcePath = inputDir.resolve(relativePath);
      var targetPath = doneDir.resolve(relativePath);

      if (!Files.exists(sourcePath)) {
        LOGGER.log(System.Logger.Level.ERROR, "Source file not found for moving: {0}", sourcePath);
        continue;
      }

      try {
        if (targetPath.getParent() != null) {
          Files.createDirectories(targetPath.getParent());
        }
        Files.move(sourcePath, targetPath, StandardCopyOption.REPLACE_EXISTING);
        LOGGER.log(
            System.Logger.Level.INFO, "Moved file successfully to done directory: {0}", targetPath);
      } catch (IOException e) {
        LOGGER.log(
            System.Logger.Level.ERROR,
            "Failed to move file " + sourcePath + ": " + e.getMessage(),
            e);
      }
    }
  }
}
