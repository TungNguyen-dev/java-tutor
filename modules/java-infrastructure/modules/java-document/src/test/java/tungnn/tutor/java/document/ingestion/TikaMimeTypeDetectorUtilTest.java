package tungnn.tutor.java.document.ingestion;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

@DisplayName("TikaUtils Unit Tests")
class TikaMimeTypeDetectorUtilTest {

  @Test
  @DisplayName("Utility class instantiation should throw UnsupportedOperationException")
  void testPrivateConstructor() throws NoSuchMethodException {
    Constructor<TikaMimeTypeDetectorUtil> constructor =
        TikaMimeTypeDetectorUtil.class.getDeclaredConstructor();
    constructor.setAccessible(true);

    InvocationTargetException exception =
        assertThrows(InvocationTargetException.class, constructor::newInstance);

    assertInstanceOf(UnsupportedOperationException.class, exception.getCause());
    assertEquals("Utility class cannot be instantiated", exception.getCause().getMessage());
  }

  @Nested
  @DisplayName("detectMimeType Tests")
  class DetectMimeTypeTests {

    @Test
    @DisplayName("Should detect text/plain MIME type successfully")
    void detectMimeType_ValidTextFile_ReturnsTextPlain(@TempDir Path tempDir) throws IOException {
      Path sampleFile = tempDir.resolve("sample.txt");
      Files.writeString(sampleFile, "Hello World Tika Test");

      String mimeType = TikaMimeTypeDetectorUtil.detectMimeType(sampleFile);

      assertEquals("text/plain", mimeType);
    }

    @Test
    @DisplayName("Should throw NullPointerException when path is null")
    void detectMimeType_NullPath_ThrowsNullPointerException() {
      NullPointerException exception =
          assertThrows(
              NullPointerException.class, () -> TikaMimeTypeDetectorUtil.detectMimeType(null));

      assertEquals("Path must not be null", exception.getMessage());
    }

    @Test
    @DisplayName("Should throw RuntimeException when path does not exist")
    void detectMimeType_NonExistentFile_ThrowsRuntimeException(@TempDir Path tempDir) {
      Path nonExistentPath = tempDir.resolve("non-existent-file.pdf");

      RuntimeException exception =
          assertThrows(
              RuntimeException.class,
              () -> TikaMimeTypeDetectorUtil.detectMimeType(nonExistentPath));

      assertTrue(exception.getMessage().contains("Failed to detect MIME type for path"));
    }
  }

  @Nested
  @DisplayName("findExtensionByMimeType Tests")
  class FindExtensionByMimeTypeTests {

    @ParameterizedTest(name = "MIME type \"{0}\" should resolve to extension \"{1}\"")
    @CsvSource({
      "application/pdf, pdf",
      "image/jpeg, jpg",
      "image/png, png",
      "text/plain, txt",
      "application/json, json"
    })
    void findExtensionByMimeType_ValidMimeTypes_ReturnsExpectedExtension(
        String mimeType, String expectedExt) {
      Optional<String> extension = TikaMimeTypeDetectorUtil.findExtensionByMimeType(mimeType);

      assertTrue(extension.isPresent());
      assertEquals(expectedExt, extension.get());
    }

    @ParameterizedTest(name = "Input \"{0}\" should return Optional.empty()")
    @NullAndEmptySource
    @ValueSource(strings = {"   ", "\t", "\n"})
    void findExtensionByMimeType_NullOrBlankInput_ReturnsEmptyOptional(String invalidInput) {
      Optional<String> result = TikaMimeTypeDetectorUtil.findExtensionByMimeType(invalidInput);

      assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("Should return Optional.empty() for unknown or invalid MIME type")
    void findExtensionByMimeType_UnknownMimeType_ReturnsEmptyOptional() {
      Optional<String> result =
          TikaMimeTypeDetectorUtil.findExtensionByMimeType("invalid/mime-type-x");

      assertTrue(result.isEmpty());
    }
  }
}
