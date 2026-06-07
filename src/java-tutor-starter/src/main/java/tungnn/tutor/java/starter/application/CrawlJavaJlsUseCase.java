package tungnn.tutor.java.starter.application;

import tungnn.tutor.java.core.lib.io.filesystem.FileNameUtil;
import tungnn.tutor.java.core.lib.io.filesystem.FileUtil;
import tungnn.tutor.java.selenium.SeleniumUtil;
import tungnn.tutor.java.selenium.page.JavaJLSPage;
import tungnn.tutor.java.starter.infrastructure.obsidian.ObsidianNote;

import java.nio.file.Path;
import java.util.List;

public class CrawlJavaJlsUseCase {

  static void main() {
    var driver = SeleniumUtil.createChromeDriver();
    try {
      var page = new JavaJLSPage(driver);
      var urls = FileUtil.readString(Path.of("storage", "jls-java26.txt")).lines().toList();
      var urlSizeWidth = String.valueOf(urls.size()).length();

      var counter = 0;
      for (var url : urls) {
        counter++;

        var result = page.crawl(url);

        var filename =
            "%s - %s.md"
                .formatted(
                    FileNameUtil.formatPaddedIndex(counter, urlSizeWidth, "Unit"),
                    FileNameUtil.sanitize(result.title()));
        var notePath = Path.of("storage", "jls26", filename);
        var note = new ObsidianNote(result.title(), result.content(), List.of(result.url()));
        writeNote(notePath, note);
      }
    } finally {
      driver.quit();
    }
  }

  private static void writeNote(Path path, ObsidianNote note) {
    FileUtil.writeString(path, note.toString());
  }
}
