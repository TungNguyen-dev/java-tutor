package tungnn.tutor.java.tool.crawler.config;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;
import tungnn.tutor.java.core.lib.io.resource.ResourceUtil;

public record AppConfig(Path baseDir, Path inputDir, Path outputDir, Path doneDir, int poolSize) {

  public static AppConfig load() {
    return load("crawler-tool.properties");
  }

  public static AppConfig load(String resourceName) {
    Properties props = new Properties();

    try (InputStream input = ResourceUtil.getResourceAsStream(resourceName)) {
      if (input == null) {
        throw new IllegalArgumentException("Resource file not found: " + resourceName);
      }
      props.load(input);
    } catch (IOException e) {
      throw new UncheckedIOException("Failed to load properties file: " + resourceName, e);
    }

    Path baseDir = Paths.get(props.getProperty("crawler.storage.base"));
    Path inputDir = baseDir.resolve(props.getProperty("crawler.dir.input"));
    Path outputDir = baseDir.resolve(props.getProperty("crawler.dir.output"));
    Path doneDir = baseDir.resolve(props.getProperty("crawler.dir.done"));
    int poolSize = Integer.parseInt(props.getProperty("crawler.pool.size"));

    return new AppConfig(baseDir, inputDir, outputDir, doneDir, poolSize);
  }
}
