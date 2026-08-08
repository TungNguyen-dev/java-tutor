package tungnn.tutor.java.core.lib.jdbc.extractor.utils;

import java.util.Locale;

public final class SchemaExtractorUtils {

  private SchemaExtractorUtils() {}

  public static String normalize(String identifier) {
    return identifier == null ? "" : identifier.toLowerCase(Locale.ROOT).trim();
  }
}
