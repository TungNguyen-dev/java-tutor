package tungnn.tutor.java.tool.shared;

import java.nio.file.Path;

public final class ExtractTextConstant {

  private ExtractTextConstant() {}

  public static final Path STORAGE_DIR = Path.of("storage", "extractor");
  public static final Path STORAGE_DIR_TODO = STORAGE_DIR.resolve("01-todo");
  public static final Path STORAGE_DIR_RESULTS = STORAGE_DIR.resolve("02-results");
}
