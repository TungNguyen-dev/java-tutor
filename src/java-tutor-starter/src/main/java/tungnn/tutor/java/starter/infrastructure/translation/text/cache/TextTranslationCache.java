package tungnn.tutor.java.starter.infrastructure.translation.text.cache;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import tungnn.tutor.java.starter.infrastructure.translation.text.TextTranslation;

public interface TextTranslationCache {

  Optional<TextTranslation> get(String sourceText, String targetLanguage);

  void put(TextTranslation translation);

  default void putAll(Collection<? extends TextTranslation> translations) {
    Objects.requireNonNull(translations, "translations must not be null").forEach(this::put);
  }

  default boolean contains(String sourceText, String targetLanguage) {
    return get(sourceText, targetLanguage).isPresent();
  }

  void invalidate(String sourceText, String targetLanguage);

  void clear();

  long size();

  List<TextTranslation> values();

  List<TextTranslation> load(String targetLanguage);

  void save(String targetLanguage);

  default void reload(String targetLanguage) {
    clear();
    putAll(load(targetLanguage));
  }

  default boolean isEmpty() {
    return size() == 0;
  }
}
