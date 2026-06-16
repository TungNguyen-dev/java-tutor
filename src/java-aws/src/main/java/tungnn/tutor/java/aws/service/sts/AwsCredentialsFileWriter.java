package tungnn.tutor.java.aws.service.sts;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
import software.amazon.awssdk.auth.credentials.AwsSessionCredentials;

public final class AwsCredentialsFileWriter {

  private static final Path CREDENTIALS_PATH =
      Path.of(System.getenv("AWS_SHARED_CREDENTIALS_FILE"));

  private AwsCredentialsFileWriter() {}

  public static void writeProfile(String profile, AwsSessionCredentials credentials) {
    try {
      Files.createDirectories(CREDENTIALS_PATH.getParent());

      var profiles = loadExistingProfiles();

      profiles.put(
          profile,
          Map.of(
              "aws_access_key_id", credentials.accessKeyId(),
              "aws_secret_access_key", credentials.secretAccessKey(),
              "aws_session_token", credentials.sessionToken()));

      writeAllProfiles(profiles);

    } catch (IOException e) {
      throw new RuntimeException("Failed to write AWS credentials file", e);
    }
  }

  private static Map<String, Map<String, String>> loadExistingProfiles() throws IOException {
    var result = new LinkedHashMap<String, Map<String, String>>();

    if (!Files.exists(CREDENTIALS_PATH)) {
      return result;
    }

    var lines = Files.readAllLines(CREDENTIALS_PATH);

    String currentProfile = null;

    for (var line : lines) {
      line = line.trim();

      if (line.isEmpty() || line.startsWith("#")) continue;

      if (line.startsWith("[") && line.endsWith("]")) {
        currentProfile = line.substring(1, line.length() - 1);
        result.putIfAbsent(currentProfile, new LinkedHashMap<>());
      } else if (currentProfile != null && line.contains("=")) {
        var parts = line.split("=", 2);
        result.get(currentProfile).put(parts[0].trim(), parts[1].trim());
      }
    }

    return result;
  }

  private static void writeAllProfiles(Map<String, Map<String, String>> profiles)
      throws IOException {

    var lines = new ArrayList<String>();

    for (var entry : profiles.entrySet()) {
      lines.add("[" + entry.getKey() + "]");
      for (var kv : entry.getValue().entrySet()) {
        lines.add(kv.getKey() + "=" + kv.getValue());
      }
      lines.add("");
    }

    Files.write(
        CREDENTIALS_PATH, lines, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
  }
}
