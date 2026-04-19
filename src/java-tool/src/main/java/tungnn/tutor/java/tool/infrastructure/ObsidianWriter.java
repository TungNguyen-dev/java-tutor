package tungnn.tutor.java.tool.infrastructure;

import tungnn.tutor.java.tool.infrastructure.dto.ObsidianWriteRequest;
import tungnn.tutor.java.tool.shared.CrawlConstant;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.Map;

public class ObsidianWriter {

  public void save(ObsidianWriteRequest request) {
    try {
      var now = OffsetDateTime.now(ZoneId.systemDefault());
      String content = buildContent(request, now);

      Path path = getResultPath(request, request.title());
      ensureDirectoryExists(path);

      Files.writeString(
          path, content, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);

    } catch (IOException e) {
      // Trong tool nội bộ, log lỗi kèm context là quan trọng nhất
      System.err.printf(
          "Failed to save Obsidian note for: %s. Error: %s%n", request.title(), e.getMessage());
    }
  }

  private String buildContent(ObsidianWriteRequest request, OffsetDateTime now) {
    var values =
        Map.of(
            "{{id}}", CrawlConstant.OBSIDIAN_NOTE_ID_FORMATTER.format(now),
            "{{noteTitle}}", sanitizeFileName(request.title()),
            "{{date}}", now.toLocalDate().toString(),
            "{{title}}", request.title(),
            "{{content}}", request.content(),
            "{{url}}", request.url());

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

  private static Path getResultPath(ObsidianWriteRequest request, String title) {
    var filename = request.unitNo() + " - " + sanitizeFileName(title) + ".md";
    return request.resultDir().resolve(filename);
  }

  private static String sanitizeFileName(String input) {
    String sanitized = input.replaceAll("[^a-zA-Z0-9._-]", "_");
    return sanitized.length() > 100 ? sanitized.substring(0, 100) : sanitized;
  }
}
