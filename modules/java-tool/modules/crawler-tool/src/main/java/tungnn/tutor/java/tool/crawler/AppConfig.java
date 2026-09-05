package tungnn.tutor.java.tool.crawler;

import java.io.IOException;
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
    var props = new Properties();

    try (var input = ResourceUtil.getResourceAsStream(resourceName)) {
      if (input == null) {
        throw new IllegalArgumentException("Resource file not found: " + resourceName);
      }
      props.load(input);
    } catch (IOException e) {
      throw new UncheckedIOException("Failed to load properties file: " + resourceName, e);
    }

    var baseDir = Paths.get(props.getProperty("crawler.storage.base"));
    var inputDir = baseDir.resolve(props.getProperty("crawler.dir.input"));
    var outputDir = baseDir.resolve(props.getProperty("crawler.dir.output"));
    var doneDir = baseDir.resolve(props.getProperty("crawler.dir.done"));
    var poolSize = Integer.parseInt(props.getProperty("crawler.pool.size"));

    return new AppConfig(baseDir, inputDir, outputDir, doneDir, poolSize);
  }
}
