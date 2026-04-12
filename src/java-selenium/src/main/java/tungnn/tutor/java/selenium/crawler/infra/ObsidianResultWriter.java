package tungnn.tutor.java.selenium.crawler.infra;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.Map;
import tungnn.tutor.java.selenium.crawler.core.model.CrawlResult;
import tungnn.tutor.java.selenium.crawler.shared.CrawlConstant;

public class ObsidianResultWriter {

  public void save(CrawlResult result) {
    try {
      var now = OffsetDateTime.now(ZoneId.systemDefault());
      String content = buildContent(result, now);

      Path path = result.resultPath();
      ensureDirectoryExists(path);

      Files.writeString(
          path, content, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);

    } catch (IOException e) {
      // Trong tool nội bộ, log lỗi kèm context là quan trọng nhất
      System.err.printf(
          "Failed to save Obsidian note for: %s. Error: %s%n", result.title(), e.getMessage());
    }
  }

  private String buildContent(CrawlResult result, OffsetDateTime now) {
    var values =
        Map.of(
            "{{id}}", CrawlConstant.OBSIDIAN_NOTE_ID_FORMATTER.format(now),
            "{{noteTitle}}", result.title(),
            "{{date}}", now.toLocalDate().toString(),
            "{{title}}", result.title(),
            "{{content}}", result.content(),
            "{{url}}", result.url());

    String template = CrawlConstant.OBSIDIAN_NOTE_TEMPLATE;
    for (var entry : values.entrySet()) {
      template = template.replace(entry.getKey(), entry.getValue());
    }
    return template;
  }

  private void ensureDirectoryExists(Path path) throws IOException {
    Path parent = path.getParent();
    if (parent != null && Files.notExists(parent)) {
      Files.createDirectories(parent);
    }
  }
}
