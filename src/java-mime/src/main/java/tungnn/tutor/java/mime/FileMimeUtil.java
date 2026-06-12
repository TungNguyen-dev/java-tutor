package tungnn.tutor.java.mime;

import org.apache.tika.Tika;
import org.apache.tika.mime.MimeType;
import org.apache.tika.mime.MimeTypes;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;

public class FileMimeUtil {

  private static final Tika TIKA = new Tika();

  public static String getMimeType(InputStream is) {
    try (is) {
      return TIKA.detect(is);
    } catch (Exception e) {
      throw new RuntimeException("Failed to detect MIME type from InputStream", e);
    }
  }

  public static String getMimeType(Path path) {
    try {
      return getMimeType(Files.newInputStream(path));
    } catch (IOException e) {
      throw new RuntimeException("Failed to detect MIME type from Path: " + path, e);
    }
  }

  public static String getExtension(String mime) {
    try {
      MimeTypes allTypes = MimeTypes.getDefaultMimeTypes();
      MimeType mimeType = allTypes.forName(mime);
      return mimeType.getExtension(); // includes dot, e.g. ".png"
    } catch (Exception e) {
      throw new RuntimeException("Failed to get extension for MIME: " + mime, e);
    }
  }
}
