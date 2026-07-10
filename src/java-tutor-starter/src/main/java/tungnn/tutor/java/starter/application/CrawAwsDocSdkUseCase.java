package tungnn.tutor.java.starter.application;

import java.nio.file.Path;
import java.util.List;
import org.openqa.selenium.WebDriver;
import tungnn.tutor.java.core.lib.io.filesystem.FileNameUtil;
import tungnn.tutor.java.core.lib.io.filesystem.FileUtil;
import tungnn.tutor.java.selenium.SeleniumUtil;
import tungnn.tutor.java.starter.infrastructure.obsidian.ObsidianNote;
import tungnn.tutor.java.starter.infrastructure.webpage.AwsDocSdkPage;

public class CrawAwsDocSdkUseCase {

  static void main() {
    WebDriver driver = null;
    try {
      driver = SeleniumUtil.createChromeDriver();
      var page = new AwsDocSdkPage(driver);
      var urls =
          FileUtil.readString(Path.of("storage", "aws-sdk-urls.txt"))
              .lines()
              .map(line -> line.strip().replace("\"", "").replace(",", ""))
              .toList();
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
        var notePath = Path.of("storage", "aws-sdk-2x", filename);
        var note = new ObsidianNote(result.title(), result.content(), List.of(result.url()));
        FileUtil.writeString(notePath, note.toString());
      }
    } catch (Exception e) {
      System.out.println("CrawAwsDocSdkUseCase main() error: " + e.getMessage());
    } finally {
      if (driver != null) {
        driver.quit();
      }
    }
  }
}
