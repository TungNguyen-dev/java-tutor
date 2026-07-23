package tungnn.tutor.java.starter.infrastructure.translation.v2.document.cache;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import tungnn.tutor.java.jackson.JsonUtil;
import tungnn.tutor.java.starter.infrastructure.translation.v2.shared.TranslationLanguage;
import tungnn.tutor.java.starter.infrastructure.translation.v2.text.model.TextTranslation;

public class FileTextTranslationCache implements TextTranslationCache {

  private static final TypeReference<List<TextTranslation>> TYPE_REFERENCE =
      new TypeReference<>() {};

  private final Map<String, TextTranslation> store = new ConcurrentHashMap<>();
  private final Path cacheDir;
  private final ObjectMapper objectMapper;

  public FileTextTranslationCache(Path cacheDir, ObjectMapper objectMapper) {
    this.cacheDir = cacheDir;
    this.objectMapper = objectMapper;
  }

  @Override
  public Optional<TextTranslation> get(String text, TranslationLanguage targetLanguage) {
    var key = getKey(text, targetLanguage);
    return Optional.ofNullable(store.get(key));
  }

  @Override
  public void put(TextTranslation translation) {
    var key = getKey(translation.sourceText(), translation.targetLanguage());
    store.put(key, translation);
  }

  @Override
  public List<TextTranslation> load(TranslationLanguage targetLanguage) {
    var cacheFile = cacheDir.resolve("cache-" + targetLanguage.code().toLowerCase() + ".json");
    return JsonUtil.readFromFile(objectMapper, cacheFile, TYPE_REFERENCE);
  }

  @Override
  public void save(TranslationLanguage targetLanguage) {
    var cacheFile = cacheDir.resolve("cache-" + targetLanguage.code().toLowerCase() + ".json");
    JsonUtil.writeToFile(objectMapper, cacheFile, store);
  }
}
