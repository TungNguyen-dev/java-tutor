package tungnn.tutor.java.starter.infrastructure.translation.text;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.concurrent.Executor;
import java.util.concurrent.Semaphore;
import java.util.function.Function;
import java.util.stream.Collectors;
import tungnn.tutor.java.core.lib.collection.CollectionChunks;
import tungnn.tutor.java.starter.infrastructure.translation.shared.LanguageCode;
import tungnn.tutor.java.starter.infrastructure.translation.text.cache.TextTranslationCache;

public abstract class AbstractTextTranslator implements TextTranslator {

  private static final int DEFAULT_CHUNK_SIZE = 50;
  private static final int DEFAULT_MAX_CONCURRENT_CHUNKS = 10;

  private final TextTranslationCache cache;
  private final Executor executor;
  private final int chunkSize;
  private final Semaphore chunkSemaphore;

  protected AbstractTextTranslator(TextTranslationCache cache, Executor executor) {
    this(cache, executor, DEFAULT_CHUNK_SIZE, DEFAULT_MAX_CONCURRENT_CHUNKS);
  }

  protected AbstractTextTranslator(
      TextTranslationCache cache, Executor executor, int chunkSize, int maxConcurrentChunks) {

    if (chunkSize <= 0) {
      throw new IllegalArgumentException("chunkSize must be positive: " + chunkSize);
    }
    if (maxConcurrentChunks <= 0) {
      throw new IllegalArgumentException(
          "maxConcurrentChunks must be positive: " + maxConcurrentChunks);
    }

    this.cache = Objects.requireNonNull(cache, "cache");
    this.executor = Objects.requireNonNull(executor, "executor");
    this.chunkSize = chunkSize;
    this.chunkSemaphore = new Semaphore(maxConcurrentChunks);
  }

  // Util
  private static String generateRequestId() {
    return UUID.randomUUID().toString();
  }

  private static Exception unwrapException(Throwable exception) {
    var actual =
        exception instanceof CompletionException completion && completion.getCause() != null
            ? completion.getCause()
            : exception;

    return actual instanceof Exception result ? result : new IllegalStateException(actual);
  }

  // Public API
  @Override
  public Collection<TextTranslation> translate(
      Collection<String> texts, LanguageCode languageCode) {

    Objects.requireNonNull(languageCode, "languageCode");
    Objects.requireNonNull(texts, "texts");

    if (texts.isEmpty()) {
      return List.of();
    }

    var uniqueTexts = new LinkedHashSet<>(texts);

    var targetLanguage = languageCode.getCode();
    cache.load(targetLanguage);

    var translations = new HashMap<String, TextTranslation>();
    var missing = new ArrayList<TextTranslationRequest>();

    for (var text : uniqueTexts) {
      cache
          .get(text, targetLanguage)
          .ifPresentOrElse(
              cached -> translations.put(text, cached),
              () -> missing.add(new TextTranslationRequest(generateRequestId(), text)));
    }

    if (!missing.isEmpty()) {
      var fresh = translateMissing(missing, languageCode);
      fresh.forEach(
          t -> {
            translations.put(t.sourceText(), t);
            cache.put(t);
          });
      cache.save(targetLanguage);
    }

    return uniqueTexts.stream().map(translations::get).filter(Objects::nonNull).toList();
  }

  // Extension points
  protected abstract Collection<TextTranslationResponse> doTranslate(
      Collection<TextTranslationRequest> requests, LanguageCode languageCode);

  protected void onTranslateChunkFailed(
      Collection<TextTranslationRequest> chunk, LanguageCode languageCode, Exception exception) {

    System.err.printf(
        "Translation chunk failed: size=%d, target=%s, cause=%s%n",
        chunk.size(), languageCode.getCode(), exception);
  }

  // Workflow
  private Collection<TextTranslation> translateMissing(
      Collection<TextTranslationRequest> missing, LanguageCode languageCode) {

    var results =
        CollectionChunks.chunkStream(missing, chunkSize)
            .map(chunk -> translateChunkAsync(chunk, languageCode))
            .toList()
            .stream()
            .map(CompletableFuture::join)
            .toList();

    results.stream()
        .filter(ChunkTranslationResult::failed)
        .forEach(
            result -> onTranslateChunkFailed(result.chunk(), languageCode, result.exception()));

    return results.stream()
        .filter(result -> !result.failed())
        .map(ChunkTranslationResult::translations)
        .flatMap(Collection::stream)
        .toList();
  }

  private CompletableFuture<ChunkTranslationResult> translateChunkAsync(
      Collection<TextTranslationRequest> chunk, LanguageCode languageCode) {

    return CompletableFuture.supplyAsync(
            () ->
                ChunkTranslationResult.success(
                    chunk, translateChunkWithPermit(chunk, languageCode)),
            executor)
        .exceptionally(
            exception -> ChunkTranslationResult.failure(chunk, unwrapException(exception)));
  }

  private Collection<TextTranslation> translateChunkWithPermit(
      Collection<TextTranslationRequest> chunk, LanguageCode languageCode) {

    try {
      chunkSemaphore.acquire();
    } catch (InterruptedException exception) {
      Thread.currentThread().interrupt();
      throw new CompletionException(exception);
    }

    try {
      return translateChunk(chunk, languageCode);
    } finally {
      chunkSemaphore.release();
    }
  }

  private Collection<TextTranslation> translateChunk(
      Collection<TextTranslationRequest> chunk, LanguageCode languageCode) {

    var requestMap =
        chunk.stream()
            .collect(Collectors.toMap(TextTranslationRequest::requestId, Function.identity()));

    return doTranslate(chunk, languageCode).stream()
        .map(response -> toTextTranslation(response, requestMap))
        .toList();
  }

  // Mapping
  private TextTranslation toTextTranslation(
      TextTranslationResponse response, Map<String, TextTranslationRequest> requests) {

    var request = requests.get(response.requestId());
    if (request == null) {
      throw new IllegalStateException(
          "Unknown translation response requestId: " + response.requestId());
    }

    var translation = response.textTranslation();
    return new TextTranslation(
        request.sourceText(),
        translation.sourceLanguage(),
        translation.targetText(),
        translation.targetLanguage());
  }

  // Nested type
  private record ChunkTranslationResult(
      Collection<TextTranslationRequest> chunk,
      Collection<TextTranslation> translations,
      Exception exception) {

    static ChunkTranslationResult success(
        Collection<TextTranslationRequest> chunk, Collection<TextTranslation> translations) {
      return new ChunkTranslationResult(chunk, translations, null);
    }

    static ChunkTranslationResult failure(
        Collection<TextTranslationRequest> chunk, Exception exception) {
      return new ChunkTranslationResult(chunk, List.of(), exception);
    }

    boolean failed() {
      return exception != null;
    }
  }
}
