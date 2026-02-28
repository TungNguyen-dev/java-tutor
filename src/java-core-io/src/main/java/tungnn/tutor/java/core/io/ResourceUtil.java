package tungnn.tutor.java.core.io;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Objects;

public final class ResourceUtil {

  private ResourceUtil() {}

  public static Path getResourcePath(String resourceName) {
    Objects.requireNonNull(resourceName, "resourceName must not be null");

    ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
    URL resourceUrl = classLoader.getResource(resourceName);

    if (resourceUrl == null) {
      throw new IllegalArgumentException("Resource not found: " + resourceName);
    }

    try {
      URI uri = resourceUrl.toURI();

      if (!"file".equalsIgnoreCase(uri.getScheme())) {
        throw new IllegalStateException(
            "Resource is not a file on the filesystem (likely inside a JAR): " + resourceName);
      }

      return Paths.get(uri);

    } catch (Exception ex) {
      throw new IllegalStateException("Failed to resolve resource path: " + resourceName, ex);
    }
  }

  public static String getResourceAsString(String resourceName) throws IOException {
    Objects.requireNonNull(resourceName, "resourceName must not be null");
    return Files.readString(getResourcePath(resourceName), StandardCharsets.UTF_8);
  }

  public static InputStream getResourceAsStream(String resourceName) throws IOException {
    Objects.requireNonNull(resourceName, "resourceName must not be null");
    return Files.newInputStream(getResourcePath(resourceName), StandardOpenOption.READ);
  }
}
