package tungnn.tutor.java.core.io;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

public final class ResourceUtil {

  private static final ClassLoader CLASS_LOADER = Thread.currentThread().getContextClassLoader();

  private ResourceUtil() {}

  public static InputStream getResourceAsStream(String resourceName) {
    return CLASS_LOADER.getResourceAsStream(resourceName);
  }

  public static String getResourceAsString(String resourceName) throws IOException {
    Objects.requireNonNull(resourceName, "resourceName must not be null");

    StringBuilder builder = new StringBuilder();
    try (InputStream in = getResourceAsStream(resourceName);
        InputStreamReader isr = new InputStreamReader(in, StandardCharsets.UTF_8);
        BufferedReader br = new BufferedReader(isr)) {
      int b;
      while ((b = br.read()) != -1) {
        builder.append((char) b);
      }
    }
    return builder.toString();
  }
}
