package tungnn.tutor.java.core.io;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.stream.Stream;

public final class FileUtil {

  private FileUtil() {}

  public static boolean copyDirectory(Path source, Path target) {
    if (source == null || target == null) {
      return false;
    }

    if (!Files.exists(source) || !Files.isDirectory(source)) {
      return false;
    }

    try (Stream<Path> paths = Files.walk(source)) {
      paths.forEach(
          path -> {
            Path relative = source.relativize(path);
            Path destination = target.resolve(relative);

            try {
              if (Files.isDirectory(path)) {
                Files.createDirectories(destination);
              } else {
                Files.createDirectories(destination.getParent());
                Files.copy(
                    path,
                    destination,
                    StandardCopyOption.REPLACE_EXISTING,
                    StandardCopyOption.COPY_ATTRIBUTES);
              }
            } catch (IOException e) {
              throw new RuntimeException(e);
            }
          });
      return true;
    } catch (IOException | RuntimeException e) {
      return false;
    }
  }
}
