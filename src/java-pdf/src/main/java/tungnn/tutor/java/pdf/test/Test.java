package tungnn.tutor.java.pdf.test;

import java.io.IOException;
import java.nio.file.Path;
import tungnn.tutor.java.core.lib.io.resource.ResourceUtil;
import tungnn.tutor.java.pdf.PdfDocumentUtil;
import tungnn.tutor.java.pdf.stripper.WordAwareStripper;

public class Test {

  static void main() throws IOException {
    var stripper = new WordAwareStripper();
    var document =
        PdfDocumentUtil.readDocument(
            ResourceUtil.getResourceAsStream("sample-pdf-letter-size.pdf"));
    var chunks = PdfDocumentUtil.extractWordChunk(document, stripper);
    chunks.forEach(System.out::println);
    var outputPath = Path.of("storage", "output.pdf");
    PdfDocumentUtil.writeDocument(outputPath, chunks);
  }
}
