package tungnn.tutor.java.tool.v2.infrastructure;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FileTemplateService {

  static void main() throws IOException {
    var path =
        Path.of(
            "/Users/tungnn/SourceCode/tungnn/java/java-tutor/storage/extractor/ETS_2023/Test 02/Screenshot 2026-04-27 at 14.36.04.png.md");
    processFileContent(Files.readString(path), Path.of("demo2"), Path.of("demo1"));
  }

  /**
   * Processes raw content and either creates a new file with a title or appends to the existing one
   * based on the pattern match.
   *
   * @param rawContent The input string to process
   * @param targetPath The path to the output file
   * @param oldTargetPath The path to the old output file
   */
  public static void processFileContent(String rawContent, Path targetPath, Path oldTargetPath)
      throws IOException {
    // Regex pattern for "Questions digit-digit" (case insensitive)
    String regex = "Questions\\s+(\\d+-\\d+)";
    Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
    Matcher matcher = pattern.matcher(rawContent);

    if (matcher.find()) {
      // CASE 1: Pattern found - Extract title and overwrite/create file
      String questionRange = matcher.group(1);
      String title = "TITLE: Questions " + questionRange + "\n";
      String remainingContent = rawContent.substring(matcher.end()).trim();

      String finalOutput = title + "---------------------------\n" + remainingContent + "\n";

      // CREATE_NEW or TRUNCATE_EXISTING
      Files.writeString(targetPath, finalOutput);
      System.out.println("New template created for: " + questionRange);

    } else {
      // CASE 2: Pattern not found - Append to existing file
      String appendContent = "\n[Additional Content]\n" + rawContent + "\n";

      Files.writeString(
          oldTargetPath, appendContent, StandardOpenOption.CREATE, StandardOpenOption.APPEND);
      System.out.println("Pattern not found. Content appended to file.");
    }
  }
}
