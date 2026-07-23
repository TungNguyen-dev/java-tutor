package tungnn.tutor.java.starter.infrastructure.translation.v2.document.cache;

import java.util.List;
import java.util.Optional;
import tungnn.tutor.java.starter.infrastructure.translation.v2.shared.TranslationLanguage;
import tungnn.tutor.java.starter.infrastructure.translation.v2.text.model.TextTranslation;

public interface TextTranslationCache {

  default String getKey(String text, TranslationLanguage targetLanguage) {
    return text + "_" + targetLanguage.code().toLowerCase();
  }

  Optional<TextTranslation> get(String text, TranslationLanguage targetLanguage);

  void put(TextTranslation translation);

  default void putAll(List<TextTranslation> translations) {
    for (TextTranslation translation : translations) {
      put(translation);
    }
  }

  List<TextTranslation> load(TranslationLanguage targetLanguage);

  void save(TranslationLanguage targetLanguage);

  default void reload(TranslationLanguage targetLanguage) {
    var translations = load(targetLanguage);
    putAll(translations);
  }
}
