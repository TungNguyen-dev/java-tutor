package tungnn.tutor.java.starter.infrastructure.translation;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;
import tungnn.tutor.java.pdf.PdfDocumentUtil;
import tungnn.tutor.java.pdf.stripper.WordAwareStripper;

public class PdfDocumentTranslator extends AbstractDocumentTranslator {

  private final WordAwareStripper stripper;

  public PdfDocumentTranslator(TextTranslator textTranslator, WordAwareStripper stripper) {
    super(textTranslator);
    this.stripper = stripper;
  }

  @Override
  protected Path doTranslate(Path path) throws IOException {
    var document = PdfDocumentUtil.readDocument(Files.newInputStream(path));
    var wordChunks = PdfDocumentUtil.extractWordChunk(document, this.stripper);

    // --- 1. Assign a UUID to each chunk (preserving order) ----------------
    Map<String, WordAwareStripper.WordChunk> idToChunk = new LinkedHashMap<>();
    for (var chunk : wordChunks) {
      idToChunk.put(UUID.randomUUID().toString(), chunk);
    }

    // --- 2. Build translation requests ------------------------------------
    var requests =
        idToChunk.entrySet().stream()
            .map(e -> new TextTranslationRequest(e.getKey(), e.getValue().text()))
            .collect(Collectors.toSet());

    // --- 3. Translate -------------------------------------------------------
    var responses = textTranslator.translate(requests);

    // --- 4. Build response lookup: translationId → translatedText ----------
    Map<String, String> idToTranslation =
        responses.stream()
            .collect(
                Collectors.toMap(
                    TextTranslationResponse::translationId, TextTranslationResponse::targetText));

    // --- 5. Reconstruct translated chunks ----------------------------------
    var translatedChunks =
        idToChunk.entrySet().stream()
            .map(
                e -> {
                  var chunk = e.getValue();
                  var translatedText = idToTranslation.get(e.getKey());
                  return new WordAwareStripper.WordChunk(
                      translatedText,
                      chunk.pageNo(),
                      chunk.x(),
                      chunk.y(),
                      chunk.fontSizePt(),
                      chunk.font(),
                      chunk.width(),
                      chunk.height());
                })
            .collect(Collectors.toList());

    // --- 6. Write output ---------------------------------------------------
    var outputPath = getOutputPath(path);
    PdfDocumentUtil.writeDocument(outputPath, translatedChunks);
    return outputPath;
  }
}
