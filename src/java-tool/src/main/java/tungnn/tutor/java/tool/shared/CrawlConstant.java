package tungnn.tutor.java.tool.shared;

import tungnn.tutor.java.core.lib.io.ResourceUtil;

import java.nio.file.Path;
import java.time.Duration;
import java.time.format.DateTimeFormatter;
import java.util.Set;

public final class CrawlConstant {

  private CrawlConstant() {}

  public static final Path STORAGE_DIR = Path.of("storage", "crawler");
  public static final Path STORAGE_DIR_RESULTS = STORAGE_DIR.resolve("results");

  public static final int CRAWLER_POOL_SIZE =
      Integer.parseInt(System.getenv().getOrDefault("CRAWLER_POOL_SIZE", "1"));

  public static final String OBSIDIAN_NOTE_TEMPLATE =
      ResourceUtil.getResourceAsString("crawler/template/obsidian-note.md");
  public static final DateTimeFormatter OBSIDIAN_NOTE_ID_FORMATTER =
      DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS");

  public static final Duration WAIT_TIMEOUT = Duration.ofMinutes(5);

  public static final Set<String> STARTUP_URLS =
      Set.of(
          "https://www.coursera.org/programs/fpt-software-complete-learning-program-me8hh?authProvider=fpt-software");
}
