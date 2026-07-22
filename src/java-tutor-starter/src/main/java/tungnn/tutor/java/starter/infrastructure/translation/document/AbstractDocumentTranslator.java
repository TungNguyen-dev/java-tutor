package tungnn.tutor.java.starter.infrastructure.translation.document;

import java.nio.file.Path;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import tungnn.tutor.java.core.lib.io.filesystem.FileNameUtil;
import tungnn.tutor.java.starter.infrastructure.translation.document.model.TranslationRequest;
import tungnn.tutor.java.starter.infrastructure.translation.document.model.TranslationResponse;
import tungnn.tutor.java.starter.infrastructure.translation.shared.LanguageCode;
import tungnn.tutor.java.starter.infrastructure.translation.text.TextTranslation;
import tungnn.tutor.java.starter.infrastructure.translation.text.TextTranslator2;
import tungnn.tutor.java.starter.infrastructure.translation.text.TranslateOption;

public abstract class AbstractDocumentTranslator implements DocumentTranslator {

  protected final Map<Path, AutoCloseable> documentMap = new ConcurrentHashMap<>();

  protected final TextTranslator2 textTranslator;

  protected AbstractDocumentTranslator(TextTranslator2 textTranslator) {
    this.textTranslator = textTranslator;
  }

  @Override
  public TranslationResponse translate(TranslationRequest request) {
    var docPath = request.docPath();
    var languageCode = request.targetLanguage();

    openDocument(docPath);

    try {
      var textRefs = collectTextRefs(docPath);
      var texts = textRefs.stream().map(TextRef::getText).toList();

      var context = getContext(docPath);
      var glossaryFiles = getGlossaryFiles(List.of(LanguageCode.JA));
      var option = new TranslateOption(context, glossaryFiles);

      var textTranslationMap =
          textTranslator.translate(texts, languageCode, option).stream()
              .collect(Collectors.toMap(TextTranslation::sourceText, TextTranslation::targetText));

      textRefs.forEach(textRef -> textRef.setText(textTranslationMap.get(textRef.getText())));

      var translationPath = parseTranslationPath(request);
      saveDocument(docPath, translationPath);

      return new TranslationResponse(translationPath);
    } finally {
      closeDocument(docPath);
    }
  }

  protected Path parseTranslationPath(TranslationRequest request) {
    var path = request.docPath();
    return path.getParent()
        .resolve(
            FileNameUtil.appendFilenameSuffix(
                path.getFileName().toString(),
                "_%s_".formatted(request.targetLanguage().getCode().toUpperCase())
                    + Instant.now().toEpochMilli()));
  }

  protected abstract void openDocument(Path docPath);

  protected abstract void saveDocument(Path docPath, Path translationPath);

  protected void closeDocument(Path docPath) {
    var document = documentMap.remove(docPath);
    if (document != null) {
      try {
        document.close();
      } catch (Exception e) {
        throw new RuntimeException(e);
      }
    }
  }

  protected abstract List<TextRef> collectTextRefs(Path docPath);

  protected abstract String getContext(Path docPath);

  protected List<Path> getGlossaryFiles(List<LanguageCode> languageCodes) {
    var dir = Path.of(System.getenv("TRANSLATION_GLOSSARY_DIR"));
    return languageCodes.stream()
        .map(
            languageCode ->
                dir.resolve("glossary-file-%s.txt".formatted(languageCode.getCode().toLowerCase())))
        .toList();
  }
}
