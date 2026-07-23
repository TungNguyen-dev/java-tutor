package tungnn.tutor.java.starter.infrastructure.translation.v1.shared;

import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public enum LanguageCode {
  VI("vi", "Vietnamese"),
  EN("en", "English"),
  JA("ja", "Japanese"),
  KO("ko", "Korean"),
  ZH("zh", "Chinese"),
  FR("fr", "French"),
  DE("de", "German");

  // Pre-built lookup for O(1), case-insensitive parsing.
  private static final Map<String, LanguageCode> BY_CODE =
      Stream.of(values()).collect(Collectors.toUnmodifiableMap(l -> l.code, Function.identity()));
  private final String code;
  private final String languageName;

  LanguageCode(String code, String languageName) {
    this.code = code;
    this.languageName = languageName;
  }

  /** Parse an ISO code ("en", "EN", " vi ") -> Optional. */
  public static Optional<LanguageCode> fromCode(String code) {
    if (code == null || code.isBlank()) return Optional.empty();
    return Optional.ofNullable(BY_CODE.get(code.strip().toLowerCase()));
  }

  /** Strict variant: throws if the code is unknown. */
  public static LanguageCode requireCode(String code) {
    return fromCode(code)
        .orElseThrow(() -> new IllegalArgumentException("Unsupported language code: " + code));
  }

  public String getCode() {
    return code;
  }

  public String getLanguageName() {
    return languageName;
  }
}
