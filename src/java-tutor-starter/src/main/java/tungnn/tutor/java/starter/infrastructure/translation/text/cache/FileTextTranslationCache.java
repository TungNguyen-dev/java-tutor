package tungnn.tutor.java.starter.infrastructure.translation.text.cache;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Stream;
import tungnn.tutor.java.starter.infrastructure.translation.text.TextTranslation;

public class FileTextTranslationCache implements TextTranslationCache {

  private static final char KEY_SEPARATOR = '\u0001';
  private static final String JSON_EXTENSION = ".json";
  private static final String CACHE_FILE_NAME = "cache.json";
  private static final TypeReference<List<TextTranslation>> TRANSLATION_LIST_TYPE =
      new TypeReference<>() {};

  private final Map<String, TextTranslation> store = new ConcurrentHashMap<>();
  private final Path storageDir;
  private final ObjectMapper objectMapper;

  public FileTextTranslationCache(Path storageDir, ObjectMapper objectMapper) {
    this.storageDir = Objects.requireNonNull(storageDir, "storageDir must not be null");
    this.objectMapper = Objects.requireNonNull(objectMapper, "objectMapper must not be null");
  }

  private static boolean isJsonFile(Path file) {
    return file.getFileName().toString().toLowerCase().endsWith(JSON_EXTENSION);
  }

  private static String normalizeTargetLanguage(String targetLanguage) {
    String value = Objects.requireNonNull(targetLanguage, "targetLanguage must not be null").trim();
    if (value.isEmpty()) {
      throw new IllegalArgumentException("targetLanguage must not be blank");
    }
    return value.toLowerCase();
  }

  private static String toSafePathName(String value) {
    return value.replaceAll("[^a-zA-Z0-9._-]", "_");
  }

  @Override
  public Optional<TextTranslation> get(String sourceText, String targetLanguage) {
    return Optional.ofNullable(store.get(cacheKey(sourceText, targetLanguage)));
  }

  @Override
  public void put(TextTranslation translation) {
    Objects.requireNonNull(translation, "translation must not be null");
    store.put(cacheKey(translation.sourceText(), translation.targetLanguage()), translation);
  }

  @Override
  public void putAll(Collection<? extends TextTranslation> translations) {
    Objects.requireNonNull(translations, "translations must not be null").forEach(this::put);
  }

  @Override
  public boolean contains(String sourceText, String targetLanguage) {
    return store.containsKey(cacheKey(sourceText, targetLanguage));
  }

  @Override
  public void invalidate(String sourceText, String targetLanguage) {
    store.remove(cacheKey(sourceText, targetLanguage));
  }

  @Override
  public void clear() {
    store.clear();
  }

  @Override
  public long size() {
    return store.size();
  }

  @Override
  public List<TextTranslation> values() {
    return List.copyOf(store.values());
  }

  @Override
  public List<TextTranslation> load(String targetLanguage) {
    String normalized = normalizeTargetLanguage(targetLanguage);
    Path targetDir = storageDirFor(normalized);

    if (Files.notExists(targetDir)) {
      return List.of();
    }
    if (!Files.isDirectory(targetDir)) {
      throw new IllegalStateException(
          "Text translation cache path is not a directory: " + targetDir);
    }

    try (Stream<Path> files = Files.list(targetDir)) {
      List<Path> jsonFiles =
          files.filter(Files::isRegularFile).filter(FileTextTranslationCache::isJsonFile).toList();

      return jsonFiles.parallelStream()
          .flatMap(file -> readTranslations(file).stream())
          .filter(
              translation ->
                  normalized.equals(normalizeTargetLanguage(translation.targetLanguage())))
          .toList();
    } catch (IOException e) {
      throw new IllegalStateException(
          "Failed to load text translation cache from directory: " + targetDir, e);
    }
  }

  @Override
  public void save(String targetLanguage) {
    String normalized = normalizeTargetLanguage(targetLanguage);
    Path targetDir = storageDirFor(normalized);
    Path cacheFile = targetDir.resolve(CACHE_FILE_NAME);

    List<TextTranslation> translations =
        store.values().stream()
            .filter(
                translation ->
                    normalized.equals(normalizeTargetLanguage(translation.targetLanguage())))
            .toList();

    try {
      Files.createDirectories(targetDir);
      objectMapper.writerWithDefaultPrettyPrinter().writeValue(cacheFile.toFile(), translations);
    } catch (IOException e) {
      throw new IllegalStateException(
          "Failed to save text translation cache to file: " + cacheFile, e);
    }
  }

  private List<TextTranslation> readTranslations(Path file) {
    try {
      return objectMapper.readValue(file.toFile(), TRANSLATION_LIST_TYPE);
    } catch (IOException e) {
      throw new IllegalStateException("Failed to read text translation cache file: " + file, e);
    }
  }

  private Path storageDirFor(String normalizedLanguage) {
    return storageDir.resolve(toSafePathName(normalizedLanguage));
  }

  private String cacheKey(String sourceText, String targetLanguage) {
    return Objects.requireNonNull(sourceText, "sourceText must not be null")
        + KEY_SEPARATOR
        + normalizeTargetLanguage(targetLanguage);
  }
}
