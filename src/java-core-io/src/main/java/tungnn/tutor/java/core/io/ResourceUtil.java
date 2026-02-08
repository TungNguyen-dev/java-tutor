package tungnn.tutor.java.core.io;

import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;

public final class ResourceUtil {

  private static final ClassLoader CLASS_LOADER = ResourceUtil.class.getClassLoader();

  private ResourceUtil() {}

  public static Path getResourcePath(String filePath) {
    URL resourceUrl = FileUtil.class.getClassLoader().getResource(filePath);
    return Path.of(Objects.requireNonNull(resourceUrl).getPath());
  }

  public static String getResourceAsString(Path path) throws IOException {
    return Files.readString(path, StandardCharsets.UTF_8);
  }
}
