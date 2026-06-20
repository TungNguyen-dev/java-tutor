package tungnn.tutor.java.aws.usecase;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
import software.amazon.awssdk.auth.credentials.AwsSessionCredentials;
import software.amazon.awssdk.profiles.Profile;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.sts.StsClient;
import tungnn.tutor.java.aws.service.sts.StsUtil;

public class AwsLoginWithMfaUseCase {

  private final StsClient stsClient;

  public AwsLoginWithMfaUseCase(StsClient stsClient) {
    this.stsClient = stsClient;
  }

  public void loginWithMfa(Profile profile, String mfaCode) {
    var roleArn =
        profile
            .property("role_arn")
            .orElseThrow(() -> new IllegalStateException("Missing role_arn"));

    var mfaSerial =
        profile
            .property("mfa_serial")
            .orElseThrow(() -> new IllegalStateException("Missing mfa_serial"));

    var region = Region.of(profile.property("region").orElse("ap-southeast-1"));

    var credentials = StsUtil.assumeRoleWithCredentials(stsClient, roleArn, mfaSerial, mfaCode);

    AwsCredentialsFileWriter.writeProfile(profile.name(), credentials);
  }

  public static final class AwsCredentialsFileWriter {

    private static final Path credentialsPath =
        System.getenv("AWS_SHARED_CREDENTIALS_FILE") != null
            ? Path.of(System.getenv("AWS_SHARED_CREDENTIALS_FILE"))
            : Path.of(System.getProperty("user.home"), ".aws", "credentials");

    public static synchronized void writeProfile(
        String profile, AwsSessionCredentials credentials) {
      try {
        Files.createDirectories(credentialsPath.getParent());

        var profiles = loadProfiles();

        profiles.put(
            profile,
            Map.of(
                "aws_access_key_id", credentials.accessKeyId(),
                "aws_secret_access_key", credentials.secretAccessKey(),
                "aws_session_token", credentials.sessionToken()));

        atomicWrite(profiles);

      } catch (IOException e) {
        throw new RuntimeException("Failed to write AWS credentials file", e);
      }
    }

    // =========================
    // Load existing profiles
    // =========================

    private static Map<String, Map<String, String>> loadProfiles() throws IOException {

      var result = new LinkedHashMap<String, Map<String, String>>();

      if (!Files.exists(credentialsPath)) {
        return result;
      }

      var lines = Files.readAllLines(credentialsPath);

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

    // =========================
    // Atomic write
    // =========================

    private static void atomicWrite(Map<String, Map<String, String>> profiles) throws IOException {

      var tempFile = Files.createTempFile("aws-credentials", ".tmp");

      var lines = new ArrayList<String>();

      for (var entry : profiles.entrySet()) {
        lines.add("[" + entry.getKey() + "]");
        for (var kv : entry.getValue().entrySet()) {
          lines.add(kv.getKey() + "=" + kv.getValue());
        }
        lines.add("");
      }

      Files.write(tempFile, lines, StandardOpenOption.TRUNCATE_EXISTING);

      Files.move(
          tempFile,
          credentialsPath,
          java.nio.file.StandardCopyOption.REPLACE_EXISTING,
          java.nio.file.StandardCopyOption.ATOMIC_MOVE);
    }
  }
}
