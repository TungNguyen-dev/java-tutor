package tungnn.tutor.java.starter.infrastructure.translation.v1.text.orchestrator;

import tungnn.tutor.java.core.lib.collection.CollectionChunks;
import tungnn.tutor.java.starter.infrastructure.translation.v1.exception.TranslationException;
import tungnn.tutor.java.starter.infrastructure.translation.v1.shared.TextReference;
import tungnn.tutor.java.starter.infrastructure.translation.v1.text.TextTranslationRequest;
import tungnn.tutor.java.starter.infrastructure.translation.v1.text.TextTranslationResponse;
import tungnn.tutor.java.starter.infrastructure.translation.v1.text.TextTranslator;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.Semaphore;

public class DefaultTextTranslatorOrchestrator implements TextTranslatorOrchestrator {

  private final TextTranslator textTranslator;
  private final int chunkSize;
  private final Executor executor;
  private final Semaphore semaphore;

  public DefaultTextTranslatorOrchestrator(
      TextTranslator textTranslator, int chunkSize, Executor executor, int maxConcurrency) {

    this.textTranslator = textTranslator;
    this.chunkSize = chunkSize;
    this.executor = executor;
    this.semaphore = new Semaphore(maxConcurrency);
  }

  @Override
  public TranslationResult translate(
      List<TextReference> textReferences, TranslationContext context) {

    if (textReferences == null || textReferences.isEmpty()) {
      return TranslationResult.empty();
    }

    var futures =
        CollectionChunks.chunkStream(textReferences, chunkSize)
            .map(chunk -> translateChunkAsync(chunk, context))
            .toList();

    CompletableFuture.allOf(futures.toArray(CompletableFuture[]::new)).join();

    var entries = new ArrayList<TranslationResult.Entry>();
    var failures = new ArrayList<Throwable>();
    for (var future : futures) {
      var result = future.join();
      if (result.isSuccess()) {
        entries.addAll(result.entries());
      } else {
        failures.add(result.error());
      }
    }

    if (!failures.isEmpty()) {
      var ex = new TranslationException(failures.size() + " chunk(s) failed to translate");
      failures.forEach(ex::addSuppressed);
      throw ex;
    }
    return new TranslationResult(entries);
  }

  private CompletableFuture<ChunkTranslateResult> translateChunkAsync(
      List<TextReference> chunk, TranslationContext context) {
    return CompletableFuture.supplyAsync(() -> translateChunk(chunk, context), executor);
  }

  private ChunkTranslateResult translateChunk(
      List<TextReference> chunk, TranslationContext context) {
    boolean acquired = false;
    try {
      semaphore.acquire();
      acquired = true;

      var idToReference = new HashMap<String, TextReference>();
      var units = new ArrayList<TextTranslationRequest.TextUnit>(chunk.size());
      for (var reference : chunk) {
        var id = generateId();
        idToReference.put(id, reference);
        units.add(new TextTranslationRequest.TextUnit(id, reference.getText()));
      }

      var request = new TextTranslationRequest(units, context.targetLanguage());
      var response = textTranslator.translate(request);
      return ChunkTranslateResult.success(buildResult(idToReference, response));
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
      return ChunkTranslateResult.fail(e);
    } catch (Exception e) {
      return ChunkTranslateResult.fail(e);
    } finally {
      if (acquired) {
        semaphore.release();
      }
    }
  }

  private List<TranslationResult.Entry> buildResult(
      Map<String, TextReference> idToReference, TextTranslationResponse response) {
    var entries = new ArrayList<TranslationResult.Entry>(response.translations().size());
    for (var translated : response.translations()) {
      var reference = idToReference.get(translated.textId());
      if (reference != null) {
        entries.add(new TranslationResult.Entry(reference, translated.translatedText()));
      }
    }
    return entries;
  }

  private String generateId() {
    return UUID.randomUUID().toString().substring(0, 8);
  }

  private record ChunkTranslateResult(List<TranslationResult.Entry> entries, Throwable error) {

    public static ChunkTranslateResult success(List<TranslationResult.Entry> entries) {
      return new ChunkTranslateResult(List.copyOf(entries), null);
    }

    public static ChunkTranslateResult fail(Throwable error) {
      return new ChunkTranslateResult(List.of(), error);
    }

    public boolean isSuccess() {
      return error == null;
    }

    public boolean isFailure() {
      return error != null;
    }
  }
}
