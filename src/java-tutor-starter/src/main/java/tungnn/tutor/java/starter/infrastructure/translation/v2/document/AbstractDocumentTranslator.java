package tungnn.tutor.java.starter.infrastructure.translation.v2.document;

import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;
import tungnn.tutor.java.starter.infrastructure.translation.v2.document.cache.TextTranslationCache;
import tungnn.tutor.java.starter.infrastructure.translation.v2.document.exception.DocumentTranslationException;
import tungnn.tutor.java.starter.infrastructure.translation.v2.document.model.DocumentTextEnriched;
import tungnn.tutor.java.starter.infrastructure.translation.v2.document.model.DocumentTranslationRequest;
import tungnn.tutor.java.starter.infrastructure.translation.v2.document.model.DocumentTranslationResponse;
import tungnn.tutor.java.starter.infrastructure.translation.v2.shared.TranslationLanguage;
import tungnn.tutor.java.starter.infrastructure.translation.v2.text.TextTranslator;
import tungnn.tutor.java.starter.infrastructure.translation.v2.text.model.TextTranslation;
import tungnn.tutor.java.starter.infrastructure.translation.v2.text.model.TextTranslationRequest;

public abstract class AbstractDocumentTranslator<D extends AutoCloseable>
    implements DocumentTranslator {

  private final TextTranslator textTranslator;
  private final TextTranslationCache textTranslationCache;

  protected AbstractDocumentTranslator(
      TextTranslator textTranslator, TextTranslationCache textTranslationCache) {
    this.textTranslator = textTranslator;
    this.textTranslationCache = textTranslationCache;
  }

  @Override
  public DocumentTranslationResponse translate(DocumentTranslationRequest request) {
    try (var document = openDocument(request)) {
      var targetLanguage = request.targetLanguage();
      var documentTexts = collectTexts(document);
      var enrichedByText = enrichByDocumentText(documentTexts);

      var translationMap = translateDistinctLines(enrichedByText, targetLanguage);
      applyTranslations(enrichedByText, translationMap);

      var translationPath = parseTranslationPath(request);
      saveDocument(document, translationPath);

      return new DocumentTranslationResponse(translationPath);
    } catch (Exception e) {
      throw new DocumentTranslationException("Failed to translate document: " + request, e);
    }
  }

  protected abstract D openDocument(DocumentTranslationRequest request);

  protected abstract List<DocumentText> collectTexts(D document);

  protected abstract void saveDocument(D document, Path path);

  private Map<DocumentText, List<DocumentTextEnriched>> enrichByDocumentText(
      List<DocumentText> documentTexts) {
    var result = new LinkedHashMap<DocumentText, List<DocumentTextEnriched>>();
    for (var documentText : documentTexts) {
      result.put(documentText, enrichText(documentText));
    }
    return result;
  }

  private List<DocumentTextEnriched> enrichText(DocumentText documentText) {
    var lines = documentText.getText().lines().toList();
    var result = new ArrayList<DocumentTextEnriched>(lines.size());
    for (var i = 0; i < lines.size(); i++) {
      result.add(new DocumentTextEnriched(documentText, lines.get(i), i));
    }
    return result;
  }

  private Map<String, String> translateDistinctLines(
      Map<DocumentText, List<DocumentTextEnriched>> enrichedByText,
      TranslationLanguage targetLanguage) {

    var distinctLines =
        enrichedByText.values().stream()
            .flatMap(List::stream)
            .map(DocumentTextEnriched::text)
            .filter(line -> !line.isBlank())
            .distinct()
            .toList();

    textTranslationCache.reload(targetLanguage);

    var translations = new ArrayList<TextTranslation>();
    var pendingRequests = new ArrayList<TextTranslationRequest>();
    for (var line : distinctLines) {
      textTranslationCache
          .get(line, targetLanguage)
          .ifPresentOrElse(
              translations::add,
              () -> pendingRequests.add(createTextTranslationRequest(line, targetLanguage)));
    }

    if (!pendingRequests.isEmpty()) {
      var responses = textTranslator.translate(pendingRequests);
      var fresh = mapToTranslations(pendingRequests, responses);
      translations.addAll(fresh);
      textTranslationCache.putAll(fresh);
      textTranslationCache.save(targetLanguage);
    }

    return translations.stream()
        .collect(
            Collectors.toMap(
                TextTranslation::sourceText, TextTranslation::targetText, (a, b) -> a));
  }

  private void applyTranslations(
      Map<DocumentText, List<DocumentTextEnriched>> enrichedByText,
      Map<String, String> translationMap) {

    enrichedByText.forEach(
        (documentText, enriched) -> {
          var translated =
              enriched.stream()
                  .sorted(Comparator.comparingInt(DocumentTextEnriched::order))
                  .map(line -> translationMap.getOrDefault(line.text(), line.text()))
                  .collect(Collectors.joining(System.lineSeparator()));
          documentText.setText(translated);
        });
  }

  private TextTranslationRequest createTextTranslationRequest(
      String text, TranslationLanguage language) {
    return new TextTranslationRequest(UUID.randomUUID().toString(), text, language);
  }
}
