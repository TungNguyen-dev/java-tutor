package tungnn.tutor.java.core.lib.io;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Objects;

public final class ResourcesReader {

  private ResourcesReader() {}

  private static final ClassLoader classLoader = ResourcesReader.class.getClassLoader();

  public static String read(String path) throws IOException {
    try (InputStream is = classLoader.getResourceAsStream(path);
        BufferedReader br = new BufferedReader(new InputStreamReader(Objects.requireNonNull(is)))) {

      StringBuilder sb = new StringBuilder();
      String line;
      while ((line = br.readLine()) != null) {
        sb.append(line);
        sb.append("\n");
      }
      return sb.toString();
    }
  }
}
