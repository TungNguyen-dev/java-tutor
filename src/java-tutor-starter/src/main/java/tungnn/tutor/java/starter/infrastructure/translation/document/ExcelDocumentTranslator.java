package tungnn.tutor.java.starter.infrastructure.translation.document;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import tungnn.tutor.java.document.excel.ExcelUtil;
import tungnn.tutor.java.starter.infrastructure.translation.shared.LanguageCode;
import tungnn.tutor.java.starter.infrastructure.translation.text.TextTranslation;
import tungnn.tutor.java.starter.infrastructure.translation.text.TextTranslator;

public class ExcelDocumentTranslator extends AbstractDocumentTranslator {

  private static final String EXCLUDE_TOKENS_PROPERTY = "translation.excel.exclude-tokens";
  private static final String EXCLUDE_TOKENS_ENV = "TRANSLATION_EXCEL_EXCLUDE_TOKENS";
  private static final Set<String> DEFAULT_EXCLUDE_TOKENS =
      Set.of("○", "X", "×", "✕", "✓", "✔", "－", "-", "—", "–", "...", "…", "O", "I");

  private final Set<String> excludeTokens;

  public ExcelDocumentTranslator(TextTranslator textTranslator) {
    this(textTranslator, resolveExcludeTokens());
  }

  public ExcelDocumentTranslator(TextTranslator textTranslator, Set<String> excludeTokens) {
    super(textTranslator);
    this.excludeTokens =
        (excludeTokens == null || excludeTokens.isEmpty())
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
        var wb = ExcelUtil.readWorkbook(is);
        var os = Files.newOutputStream(outputPath)) {

      var textToCells = new HashMap<String, List<Cell>>();
      for (var sheet : wb) {
        for (var row : sheet) {
          for (var cell : row) {
            if (shouldNotTranslateCell(cell)) {
              continue;
            }
            var value = ExcelUtil.getCellContent(cell).toString().strip();
            if (shouldNotTranslateCellValue(value)) {
              continue;
            }
            textToCells.computeIfAbsent(value, k -> new ArrayList<>()).add(cell);
          }
        }
      }

      if (textToCells.isEmpty()) {
        return;
      }

      var translations = translate(textToCells.keySet(), targetLanguage);
      textToCells.forEach(
          (source, cells) -> {
            var translated = translations.get(source);
            if (translated != null) {
              cells.forEach(cell -> cell.setCellValue(translated));
            }
          });

      wb.write(os);
    }
  }

  private Map<String, String> translate(Collection<String> texts, LanguageCode targetLanguage) {
    return textTranslator.translate(texts, targetLanguage).stream()
        .collect(
            Collectors.toMap(
                TextTranslation::sourceText,
                TextTranslation::targetText,
                (existing, ignored) -> existing));
  }

  private boolean shouldNotTranslateCell(Cell cell) {
    if (cell == null) {
      return true;
    }
    var type = cell.getCellType();
    if (type == CellType.STRING) {
      return false;
    }
    return type != CellType.FORMULA || cell.getCachedFormulaResultType() != CellType.STRING;
  }

  private boolean shouldNotTranslateCellValue(String value) {
    if (value == null || value.isBlank()) {
      return true;
    }
    var v = value.strip();
    return excludeTokens.contains(v) || hasNoLetter(v);
  }

  private boolean hasNoLetter(String value) {
    return value.codePoints().noneMatch(Character::isLetter);
  }
}
