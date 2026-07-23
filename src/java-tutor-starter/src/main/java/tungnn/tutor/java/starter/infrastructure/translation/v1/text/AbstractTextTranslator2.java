package tungnn.tutor.java.starter.infrastructure.translation.v1.text;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.concurrent.Executor;
import java.util.concurrent.Semaphore;
import java.util.function.Function;
import java.util.stream.Collectors;
import tungnn.tutor.java.core.lib.collection.CollectionChunks;
import tungnn.tutor.java.starter.infrastructure.translation.v1.shared.LanguageCode;
import tungnn.tutor.java.starter.infrastructure.translation.v1.text.cache.TextTranslationCache;

public abstract class AbstractTextTranslator2 implements TextTranslator2 {

  private static final int DEFAULT_CHUNK_SIZE = 50;
  private static final int DEFAULT_MAX_CONCURRENT_CHUNKS = 10;

  private final TextTranslationCache cache;
  private final Executor executor;
  private final int chunkSize;
  private final Semaphore chunkSemaphore;

  public AbstractTextTranslator2(TextTranslationCache cache, Executor executor) {
    this(cache, executor, DEFAULT_CHUNK_SIZE, DEFAULT_MAX_CONCURRENT_CHUNKS);
  }

  public AbstractTextTranslator2(
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
    return UUID.randomUUID().toString().substring(0, 8);
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
  public List<TextTranslation> translate(
      List<String> texts, LanguageCode languageCode, TranslateOption option) {

    Objects.requireNonNull(languageCode, "languageCode");
    Objects.requireNonNull(texts, "texts");
    Objects.requireNonNull(option, "option");

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
      var fresh = translateMissing(missing, languageCode, option);
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
  protected abstract List<TextTranslationResponse> doTranslate(
      List<TextTranslationRequest> requests,
      LanguageCode languageCode,
      TranslateChunkOption option);

  protected void onTranslateChunkFailed(
      List<TextTranslationRequest> chunk, LanguageCode languageCode, Exception exception) {

    System.err.printf(
        "Translation chunk failed: size=%d, target=%s, cause=%s%n",
        chunk.size(), languageCode.getCode(), exception);
  }

  // Workflow
  private List<TextTranslation> translateMissing(
      List<TextTranslationRequest> missing, LanguageCode languageCode, TranslateOption option) {

    var results =
        CollectionChunks.chunkStream(missing, chunkSize)
            .map(chunk -> translateChunkAsync(chunk, languageCode, option))
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
        .flatMap(List::stream)
        .toList();
  }

  private CompletableFuture<ChunkTranslationResult> translateChunkAsync(
      List<TextTranslationRequest> chunk, LanguageCode languageCode, TranslateOption option) {

    return CompletableFuture.supplyAsync(
            () ->
                ChunkTranslationResult.success(
                    chunk, translateChunkWithPermit(chunk, languageCode, option)),
            executor)
        .exceptionally(
            exception -> ChunkTranslationResult.failure(chunk, unwrapException(exception)));
  }

  private List<TextTranslation> translateChunkWithPermit(
      List<TextTranslationRequest> chunk, LanguageCode languageCode, TranslateOption option) {

    try {
      chunkSemaphore.acquire();
    } catch (InterruptedException exception) {
      Thread.currentThread().interrupt();
      throw new CompletionException(exception);
    }

    try {
      return translateChunk(chunk, languageCode, option);
    } finally {
      chunkSemaphore.release();
    }
  }

  private List<TextTranslation> translateChunk(
      List<TextTranslationRequest> chunk, LanguageCode languageCode, TranslateOption option) {

    var requestMap =
        chunk.stream()
            .collect(Collectors.toMap(TextTranslationRequest::requestId, Function.identity()));
    var chunkOption =
        new TranslateChunkOption(buildChunkContextFile(chunk, option), option.glossaryFiles());

    return doTranslate(chunk, languageCode, chunkOption).stream()
        .map(response -> toTextTranslation(response, requestMap))
        .toList();
  }

  private Path buildChunkContextFile(List<TextTranslationRequest> chunk, TranslateOption option) {
    var sb = new StringBuilder();
    var contextLines = option.context().lines().toList();
    for (var request : chunk) {
      var text = request.sourceText();
      var index = contextLines.indexOf(text);
      var subLines = contextLines.subList(index - 5, index + 5);
      sb.repeat("-", 80).append(System.lineSeparator());
      subLines.forEach(line -> sb.append(line).append(System.lineSeparator()));
      sb.repeat("-", 80).append(System.lineSeparator());
    }
    var chunkContext = sb.toString();
    try {
      var tmp = Files.createTempFile("text-translate-chunk-context-", ".txt");
      Files.writeString(tmp, chunkContext);
      return tmp;
    } catch (Exception ex) {
      throw new RuntimeException(ex);
    }
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
      List<TextTranslationRequest> chunk, List<TextTranslation> translations, Exception exception) {

    static ChunkTranslationResult success(
        List<TextTranslationRequest> chunk, List<TextTranslation> translations) {
      return new ChunkTranslationResult(chunk, translations, null);
    }

    static ChunkTranslationResult failure(List<TextTranslationRequest> chunk, Exception exception) {
      return new ChunkTranslationResult(chunk, List.of(), exception);
    }

    boolean failed() {
      return exception != null;
    }
  }
}
