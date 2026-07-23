package tungnn.tutor.java.starter.infrastructure.translation.v2.document;

import java.nio.file.Path;
import java.time.Instant;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import tungnn.tutor.java.core.lib.io.filesystem.FileNameUtil;
import tungnn.tutor.java.starter.infrastructure.translation.v2.document.model.DocumentTranslationRequest;
import tungnn.tutor.java.starter.infrastructure.translation.v2.document.model.DocumentTranslationResponse;
import tungnn.tutor.java.starter.infrastructure.translation.v2.text.model.TextTranslation;
import tungnn.tutor.java.starter.infrastructure.translation.v2.text.model.TextTranslationRequest;
import tungnn.tutor.java.starter.infrastructure.translation.v2.text.model.TextTranslationResponse;

public interface DocumentTranslator {

  DocumentTranslationResponse translate(DocumentTranslationRequest request);

  default List<TextTranslation> mapToTranslations(
      List<TextTranslationRequest> requests, List<TextTranslationResponse> responses) {

    if (requests.size() != responses.size()) {
      throw new IllegalArgumentException("requests and responses must have same size");
    }

    var responseMap =
        responses.stream()
            .collect(Collectors.toMap(TextTranslationResponse::requestId, Function.identity()));

    return requests.stream()
        .map(request -> mapToTranslation(request, responseMap.get(request.requestId())))
        .toList();
  }

  default TextTranslation mapToTranslation(
      TextTranslationRequest request, TextTranslationResponse response) {

    return new TextTranslation(
        request.sourceText(),
        request.sourceLanguage(),
        response.targetText(),
        response.targetLanguage());
  }

  default Path parseTranslationPath(DocumentTranslationRequest request) {
    var path = request.documentPath();
    return path.getParent()
        .resolve(
            FileNameUtil.appendFilenameSuffix(
                path.getFileName().toString(),
                "_%s_".formatted(request.targetLanguage().code().toUpperCase())
                    + Instant.now().toEpochMilli()));
  }
}
