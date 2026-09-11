package tungnn.tutor.java.document.ingestion;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;
import java.util.Optional;
import org.apache.tika.Tika;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.metadata.TikaCoreProperties;
import org.apache.tika.mime.MimeType;
import org.apache.tika.mime.MimeTypeException;
import org.apache.tika.mime.MimeTypes;

public final class TikaMimeTypeDetectorUtil {

  private static final Tika TIKA = new Tika();
  private static final MimeTypes MIME_TYPES = MimeTypes.getDefaultMimeTypes();

  private TikaMimeTypeDetectorUtil() {
    throw new UnsupportedOperationException("Utility class cannot be instantiated");
  }

  public static String detectMimeType(Path path) {
    Objects.requireNonNull(path, "Path must not be null");

    Metadata metadata = new Metadata();
    metadata.set(TikaCoreProperties.RESOURCE_NAME_KEY, path.getFileName().toString());

    try (InputStream inputStream = Files.newInputStream(path)) {
      return TIKA.detect(inputStream, metadata);
    } catch (Exception e) {
      throw new RuntimeException("Failed to detect MIME type for path: " + path, e);
    }
  }

  public static Optional<String> findExtensionByMimeType(String mimeTypeName) {
    if (mimeTypeName == null || mimeTypeName.isBlank()) {
      return Optional.empty();
    }

    try {
      MimeType mimeType = MIME_TYPES.forName(mimeTypeName);
      String ext = mimeType.getExtension();

      if (ext.isEmpty()) {
        return Optional.empty();
      }

      return Optional.of(ext.startsWith(".") ? ext.substring(1) : ext);
    } catch (MimeTypeException e) {
      return Optional.empty();
    }
  }
}
