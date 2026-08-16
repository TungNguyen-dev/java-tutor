package tungnn.tutor.java.tool.translation.service;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import tungnn.tutor.java.tool.translation.core.document.impl.ExcelDocumentTranslator;
import tungnn.tutor.java.tool.translation.core.document.impl.SlideshowDocumentTranslator;
import tungnn.tutor.java.tool.translation.core.document.impl.WordDocumentTranslator;
import tungnn.tutor.java.tool.translation.core.text.DummyTextTranslator;
import tungnn.tutor.java.tool.translation.core.text.orchestrator.DefaultTextTranslatorOrchestrator;
import tungnn.tutor.java.tool.translation.service.impl.SimpleTranslationService;

class TranslationServiceTests {

  private ExecutorService executor;
  private SimpleTranslationService translationService;

  @BeforeEach
  void setUp() {
    executor = Executors.newVirtualThreadPerTaskExecutor();
    var textTranslator = new DummyTextTranslator();
    var orchestrator = new DefaultTextTranslatorOrchestrator(textTranslator, 50, executor, 10);

    var wordTranslator = new WordDocumentTranslator(orchestrator);
    var excelTranslator = new ExcelDocumentTranslator(orchestrator);
    var slideshowTranslator = new SlideshowDocumentTranslator(orchestrator);

    translationService =
        new SimpleTranslationService(wordTranslator, excelTranslator, slideshowTranslator);
  }

  @AfterEach
  void tearDown() {
    if (executor != null) {
      executor.close();
    }
  }

  @Test
  @DisplayName("Should translate Word document and append suffix to text content")
  void shouldTranslateWordDocumentContent(@TempDir Path tempDir) throws IOException {
    // Given
    var originalText = "Hello world";
    var sourceDocumentPath = tempDir.resolve("sample.docx");
    createMockWordDocument(sourceDocumentPath, originalText);
    var targetLanguage = "vi";

    // When
    var resultPath = translationService.translateDocument(sourceDocumentPath, targetLanguage);

    // Then
    var expectedPrefix = "sample_VN_";
    var actualFileName = resultPath.getFileName().toString();
    var translatedContent = readWordDocumentContent(resultPath);

    assertAll(
        () -> assertNotNull(resultPath, "Translated file path should not be null"),
        () ->
            assertTrue(
                actualFileName.startsWith(expectedPrefix),
                () ->
                    String.format(
                        "Expected file name to start with '%s', but got '%s'",
                        expectedPrefix, actualFileName)),
        () ->
            assertEquals(
                originalText + "_translated",
                translatedContent,
                "Document content should be appended with '_translated'"));
  }

  private void createMockWordDocument(Path targetPath, String content) throws IOException {
    try (var document = new XWPFDocument();
        var out = Files.newOutputStream(targetPath)) {
      XWPFParagraph paragraph = document.createParagraph();
      var run = paragraph.createRun();
      run.setText(content);
      document.write(out);
    }
  }

  private String readWordDocumentContent(Path filePath) throws IOException {
    try (var in = Files.newInputStream(filePath);
        var document = new XWPFDocument(in)) {
      return document.getParagraphs().stream()
          .map(XWPFParagraph::getText)
          .reduce("", (a, b) -> a + b);
    }
  }
}
