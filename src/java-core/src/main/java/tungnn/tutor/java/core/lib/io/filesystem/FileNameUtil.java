package tungnn.tutor.java.core.lib.io.filesystem;

import java.text.Normalizer;
import java.util.Set;
import java.util.regex.Pattern;

public final class FileNameUtil {

  private static final int MAX_LENGTH = 255;
  private static final String DEFAULT_NAME = "default_filename";

  private static final Pattern DIACRITICS = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");

  private static final Set<String> WINDOWS_RESERVED =
      Set.of(
          "CON", "PRN", "AUX", "NUL", "COM1", "COM2", "COM3", "COM4", "COM5", "COM6", "COM7",
          "COM8", "COM9", "LPT1", "LPT2", "LPT3", "LPT4", "LPT5", "LPT6", "LPT7", "LPT8", "LPT9");

  private FileNameUtil() {}

  public static String sanitize(String input) {
    if (input == null || input.isBlank()) {
      return DEFAULT_NAME;
    }

    // 1. Normalize (remove diacritics)
    String normalized = Normalizer.normalize(input, Normalizer.Form.NFD);
    String noDiacritics = DIACRITICS.matcher(normalized).replaceAll("");

    // 2. Build sanitized string (avoid regex)
    StringBuilder sb = new StringBuilder(noDiacritics.length());

    for (char c : noDiacritics.toCharArray()) {
      if (Character.isLetterOrDigit(c) || c == '_' || c == '-' || c == '.') {
        sb.append(c);
      } else if (Character.isWhitespace(c)) {
        sb.append('_');
      }
      // ignore others
    }

    String sanitized = collapseDots(sb.toString());
    sanitized = trimUnderscores(sanitized);

    // 3. Prevent dangerous or empty names
    if (sanitized.isEmpty() || sanitized.equals(".")) {
      return DEFAULT_NAME;
    }

    // 4. Handle reserved names (Windows)
    if (WINDOWS_RESERVED.contains(sanitized.toUpperCase())) {
      sanitized = "_" + sanitized;
    }

    // 5. Enforce max length with extension preservation
    sanitized = enforceMaxLength(sanitized);

    return sanitized;
  }

  private static String collapseDots(String input) {
    StringBuilder sb = new StringBuilder(input.length());
    boolean lastWasDot = false;

    for (char c : input.toCharArray()) {
      if (c == '.') {
        if (!lastWasDot) {
          sb.append(c);
          lastWasDot = true;
        }
      } else {
        sb.append(c);
        lastWasDot = false;
      }
    }
    return sb.toString();
  }

  private static String trimUnderscores(String input) {
    int start = 0;
    int end = input.length();

    while (start < end && input.charAt(start) == '_') start++;
    while (end > start && input.charAt(end - 1) == '_') end--;

    return input.substring(start, end);
  }

  private static String enforceMaxLength(String input) {
    if (input.length() <= MAX_LENGTH) return input;

    int dot = input.lastIndexOf('.');
    if (dot > 0 && dot < input.length() - 1) {
      String ext = input.substring(dot);
      int allowed = MAX_LENGTH - ext.length();
      if (allowed > 0) {
        return input.substring(0, allowed) + ext;
      }
    }
    return input.substring(0, MAX_LENGTH);
  }

  public static String formatPaddedIndex(int number, int width, String prefix) {
    String format = prefix + " %0" + width + "d";
    return String.format(format, number);
  }

  public static String appendFilenameSuffix(String filename, String suffix) {
    int dotIndex = filename.lastIndexOf('.');

    if (dotIndex <= 0) {
      return filename.endsWith(suffix) ? filename : filename + suffix;
    }

    String name = filename.substring(0, dotIndex);
    String extension = filename.substring(dotIndex);

    if (name.endsWith(suffix)) {
      return filename;
    }

    return name + suffix + extension;
  }
}
