import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

void main(String[] args) {
  if (args.length == 0) {
    System.out.println("Vui lòng truyền đường dẫn thư mục. Ví dụ: java Main.java /path/to/dir");
    return;
  }

  Path dirPath = Paths.get(args[0]);

  if (!Files.exists(dirPath) || !Files.isDirectory(dirPath)) {
    System.err.println("Lỗi: Đường dẫn truyền vào không tồn tại hoặc không phải là thư mục.");
    return;
  }

  // Tiến hành duyệt qua tất cả các file trong thư mục
  try (Stream<Path> stream = Files.walk(dirPath)) {
    stream
        .filter(Files::isRegularFile)
        .filter(p -> p.toString().endsWith(".md"))
        .forEach(file -> extractUrlsFromFile(file));
  } catch (IOException e) {
    System.err.println("Lỗi khi duyệt thư mục: " + e.getMessage());
  }
}

void extractUrlsFromFile(Path filePath) {
  String regex = "\\b(https?://|www\\.)[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]";
  Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);

  try {
    String content = Files.readString(filePath);
    Matcher matcher = pattern.matcher(content);

    List<String> urls = new ArrayList<>();
    while (matcher.find()) {
      urls.add(matcher.group());
    }
    urls =
        urls.stream().filter(url -> url.contains("youtube") || url.contains("coursera")).toList();

    if (!urls.isEmpty()) {
      System.out.println("\n[File]: " + filePath.toAbsolutePath());
      urls.forEach(url -> System.out.println(url));
    }
  } catch (IOException e) {
    System.err.println("Không thể đọc file " + filePath + ": " + e.getMessage());
  }
}
