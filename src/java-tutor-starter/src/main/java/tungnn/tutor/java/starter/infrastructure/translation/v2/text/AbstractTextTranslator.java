package tungnn.tutor.java.starter.infrastructure.translation.v2.text;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.concurrent.Executor;
import java.util.concurrent.Semaphore;
import tungnn.tutor.java.core.lib.collection.CollectionChunks;
import tungnn.tutor.java.starter.infrastructure.translation.v2.text.model.TextTranslationRequest;
import tungnn.tutor.java.starter.infrastructure.translation.v2.text.model.TextTranslationResponse;

public abstract class AbstractTextTranslator implements TextTranslator {

  private static final int CHUNK_SIZE = 50;
  private static final int MAX_CONCURRENCY = 20;

  private final Executor executor;
  private final Semaphore semaphore;

  protected AbstractTextTranslator(Executor executor) {
    this.executor = executor;
    semaphore = new Semaphore(MAX_CONCURRENCY);
  }

  @Override
  public List<TextTranslationResponse> translate(List<TextTranslationRequest> requests) {
    return CollectionChunks.chunkStream(requests, CHUNK_SIZE)
        .map(this::doTranslateAsync)
        .toList()
        .stream()
        .map(CompletableFuture::join)
        .flatMap(List::stream)
        .toList();
  }

  private CompletableFuture<List<TextTranslationResponse>> doTranslateAsync(
      List<TextTranslationRequest> chunk) {

    return CompletableFuture.supplyAsync(() -> doTranslateWithSemaphore(chunk), executor)
        .exceptionally(
            e -> {
              System.err.printf("Failed to translate chunk: %s\n", e.getMessage());
              return List.of();
            });
  }

  private List<TextTranslationResponse> doTranslateWithSemaphore(
      List<TextTranslationRequest> requests) {

    try {
      semaphore.acquire();
    } catch (InterruptedException exception) {
      Thread.currentThread().interrupt();
      throw new CompletionException(exception);
    }

    try {
      return doTranslate(requests);
    } finally {
      semaphore.release();
    }
  }

  protected abstract List<TextTranslationResponse> doTranslate(
      List<TextTranslationRequest> requests);
}
