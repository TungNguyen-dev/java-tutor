package tungnn.tutor.java.starter.infrastructure.translation.document;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import tungnn.tutor.java.office.powerpoint.PowerPointUtil;
import tungnn.tutor.java.starter.infrastructure.translation.shared.LanguageCode;
import tungnn.tutor.java.starter.infrastructure.translation.text.TextTranslation;
import tungnn.tutor.java.starter.infrastructure.translation.text.TextTranslator;

public class PowerPointDocumentTranslator extends AbstractDocumentTranslator {

  private static final String EXCLUDE_TOKENS_PROPERTY = "translation.powerpoint.exclude-tokens";
  private static final String EXCLUDE_TOKENS_ENV = "TRANSLATION_POWERPOINT_EXCLUDE_TOKENS";

  private static final Set<String> DEFAULT_EXCLUDE_TOKENS =
      Set.of("○", "X", "×", "✕", "✓", "✔", "－", "-", "—", "–", "...", "…", "O", "I");

  private final Set<String> excludeTokens;

  public PowerPointDocumentTranslator(TextTranslator textTranslator) {
    this(textTranslator, resolveExcludeTokens());
  }

  public PowerPointDocumentTranslator(TextTranslator textTranslator, Set<String> excludeTokens) {
    super(textTranslator);
    this.excludeTokens =
        excludeTokens == null || excludeTokens.isEmpty()
            ? DEFAULT_EXCLUDE_TOKENS
            : Set.copyOf(excludeTokens);
  }

  static Set<String> resolveExcludeTokens() {
    var raw = System.getProperty(EXCLUDE_TOKENS_PROPERTY);
    if (raw == null || raw.isBlank()) {
      raw = System.getenv(EXCLUDE_TOKENS_ENV);
    }
    return parseTokens(raw);
  }

  static Set<String> parseTokens(String raw) {
    if (raw == null || raw.isBlank()) {
      return DEFAULT_EXCLUDE_TOKENS;
    }

    return Arrays.stream(raw.split(","))
        .map(String::strip)
        .filter(token -> !token.isEmpty())
        .collect(Collectors.toUnmodifiableSet());
  }

  @Override
  protected void doTranslate(Path inputPath, Path outputPath, LanguageCode targetLanguage)
      throws IOException {

    try (var is = Files.newInputStream(inputPath);
        var presentation = PowerPointUtil.readPresentation(is);
        var os = Files.newOutputStream(outputPath)) {

      var textToRuns = PowerPointUtil.collectTextRuns(presentation, this::shouldTranslateValue);

      if (!textToRuns.isEmpty()) {
        var translations = translate(textToRuns.keySet(), targetLanguage);
        PowerPointUtil.applyTranslations(textToRuns, translations);
      }

      PowerPointUtil.writePresentation(presentation, os);
    }
  }

  private Map<String, String> translate(Collection<String> texts, LanguageCode targetLanguage) {
    return textTranslator.translate(texts, targetLanguage).stream()
        .filter(translation -> translation.sourceText() != null && translation.targetText() != null)
        .collect(
            Collectors.toMap(
                TextTranslation::sourceText,
                TextTranslation::targetText,
                (existing, ignored) -> existing));
  }

  private boolean shouldTranslateValue(String value) {
    if (value == null || value.isBlank()) {
      return false;
    }

    var v = value.strip();
    return !excludeTokens.contains(v) && hasLetter(v);
  }

  private boolean hasLetter(String value) {
    return value.codePoints().anyMatch(Character::isLetter);
  }
}
