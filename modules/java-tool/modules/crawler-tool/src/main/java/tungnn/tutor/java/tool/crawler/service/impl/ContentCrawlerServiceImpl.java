package tungnn.tutor.java.tool.crawler.service.impl;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import tungnn.tutor.java.core.lib.io.filesystem.FileNameUtil;
import tungnn.tutor.java.core.lib.io.filesystem.FileUtil;
import tungnn.tutor.java.document.markdown.MarkdownWriterUtils;
import tungnn.tutor.java.mime.FileMimeUtil;
import tungnn.tutor.java.tool.crawler.config.AppConfig;
import tungnn.tutor.java.tool.crawler.core.BatchCrawlExecutor;
import tungnn.tutor.java.tool.crawler.core.ContentCrawlRequest;
import tungnn.tutor.java.tool.crawler.core.ContentCrawlResult;
import tungnn.tutor.java.tool.crawler.obsidian.ObsidianNote;
import tungnn.tutor.java.tool.crawler.service.ContentCrawlerService;

public class ContentCrawlerServiceImpl implements ContentCrawlerService {

  private final AppConfig appConfig;
  private final BatchCrawlExecutor batchCrawlExecutor;

  public ContentCrawlerServiceImpl(AppConfig appConfig, BatchCrawlExecutor batchCrawlExecutor) {
    this.appConfig = appConfig;
    this.batchCrawlExecutor = batchCrawlExecutor;
  }

  @Override
  public void crawlContent() {
    var courseSources = loadCourseSources();

    if (courseSources.isEmpty()) {
      return;
    }

    var resultsByUrl = executeBatchCrawl(courseSources);

    courseSources.forEach(courseSource -> processCourseSource(courseSource, resultsByUrl));
  }

  private List<CourseSource> loadCourseSources() {
    return FileUtil.walk(appConfig.inputDir()).stream()
        .filter(Files::isRegularFile)
        .filter(p -> FileMimeUtil.getExtension(FileMimeUtil.getMimeType(p)).equals(".txt"))
        .map(this::parseCourseSource)
        .toList();
  }

  private CourseSource parseCourseSource(Path sourcePath) {
    var targetUrls =
        FileUtil.readString(sourcePath)
            .lines()
            .map(String::trim)
            .filter(line -> !line.isBlank())
            .toList();

    return new CourseSource(sourcePath, targetUrls);
  }

  private LinkedHashMap<String, ContentCrawlResult> executeBatchCrawl(
      List<CourseSource> courseSources) {

    var crawlRequests =
        courseSources.stream()
            .map(CourseSource::targetUrls)
            .flatMap(Collection::stream)
            .distinct()
            .map(ContentCrawlRequest::new)
            .toList();

    return batchCrawlExecutor.crawlBatch(crawlRequests).stream()
        .collect(
            Collectors.toMap(
                ContentCrawlResult::url,
                result -> result,
                (existing, _) -> existing,
                LinkedHashMap::new));
  }

  private void processCourseSource(
      CourseSource courseSource, LinkedHashMap<String, ContentCrawlResult> resultsByUrl) {

    int totalUnits = courseSource.targetUrls().size();
    int zeroPaddingWidth = String.valueOf(totalUnits).length();

    var unitCounter = new AtomicInteger(0);

    for (String targetUrl : courseSource.targetUrls()) {
      int unitNumber = unitCounter.incrementAndGet();

      var result = resultsByUrl.get(targetUrl);

      if (result == null || !result.isSuccess()) {
        System.err.printf("Crawl failed [%d/%d]: %s%n", unitNumber, totalUnits, targetUrl);
        continue;
      }

      var outputDir = appConfig.outputDir().resolve(courseSource.getFileName());
      FileUtil.createDirectories(outputDir);
      writeResultToFile(outputDir, unitNumber, result, zeroPaddingWidth);
    }

    moveSourceToDoneDir(courseSource.path());
  }

  private void writeResultToFile(
      Path outputDir, int unitNumber, ContentCrawlResult result, int zeroPaddingWidth) {

    String sanitizedTitle = FileNameUtil.sanitize(result.title()).replace("_", " ");
    String formatPattern = "%0" + zeroPaddingWidth + "d - %s.md";
    String fileName = String.format(formatPattern, unitNumber, sanitizedTitle);

    Path destinationFile = outputDir.resolve(fileName);

    ObsidianNote note =
        new ObsidianNote(
            sanitizedTitle,
            MarkdownWriterUtils.convertHtmlToMarkdown(result.content()),
            List.of(result.url()));

    // TODO: Enhance note by AI

    FileUtil.writeString(destinationFile, note.toMarkdown());
  }

  private void moveSourceToDoneDir(Path sourcePath) {
    try {
      Path doneDir = FileUtil.createDirectories(appConfig.doneDir());
      Path destinationPath = doneDir.resolve(sourcePath.getFileName().toString());

      Files.move(sourcePath, destinationPath, StandardCopyOption.REPLACE_EXISTING);

    } catch (IOException e) {
      throw new UncheckedIOException(
          "Failed to move completed file to done directory: " + sourcePath, e);
    }
  }

  private record CourseSource(Path path, List<String> targetUrls) {

    private String getFileName() {
      return path.getFileName().toString();
    }
  }
}
