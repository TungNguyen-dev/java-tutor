package tungnn.tutor.java.starter.application;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Collection;
import tungnn.tutor.java.pdf.stripper.WordAwareStripper;
import tungnn.tutor.java.starter.infrastructure.translation.PdfDocumentTranslator;
import tungnn.tutor.java.starter.infrastructure.translation.TextTranslationRequest;
import tungnn.tutor.java.starter.infrastructure.translation.TextTranslationResponse;
import tungnn.tutor.java.starter.infrastructure.translation.TextTranslator;

public class TestPdfDocumentTranslator {

  static void main() throws IOException {
    var textTranslator =
        new TextTranslator() {
          @Override
          public Collection<TextTranslationResponse> translate(
              Collection<TextTranslationRequest> translationRequests) {
            return translationRequests.stream()
                .map(
                    request ->
                        new TextTranslationResponse(
                            request.translationId(),
                            request.sourceText(),
                            null,
                            request.sourceText() + " [translated]",
                            null))
                .toList();
          }
        };
    var stripper = new WordAwareStripper();
    var translator = new PdfDocumentTranslator(textTranslator, stripper);
    translator.translateDocument(Path.of("storage", "sample-pdf-letter-size.pdf"));
  }
}
