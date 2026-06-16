package tungnn.tutor.java.aws.service.sts;

import software.amazon.awssdk.auth.credentials.AwsSessionCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.services.sts.StsClient;
import software.amazon.awssdk.services.sts.model.AssumeRoleRequest;

import java.util.Objects;

public final class StsUtil {

  private StsUtil() {
    throw new UnsupportedOperationException("Utility class");
  }

  public static AwsSessionCredentials assumeRoleCredentials(
      StsClient stsClient,
      String roleArn,
      String roleSessionName,
      String mfaSerialNumber,
      String mfaTokenCode) {

    Objects.requireNonNull(stsClient);
    Objects.requireNonNull(roleArn);
    Objects.requireNonNull(roleSessionName);

    var builder = AssumeRoleRequest.builder().roleArn(roleArn).roleSessionName(roleSessionName);

    if (mfaSerialNumber != null && mfaTokenCode != null) {
      builder.serialNumber(mfaSerialNumber).tokenCode(mfaTokenCode);
    }

    var response = stsClient.assumeRole(builder.build());

    var credentials = response.credentials();
    if (credentials == null) {
      throw new IllegalStateException("Missing credentials in STS response");
    }

    return AwsSessionCredentials.create(
        credentials.accessKeyId(), credentials.secretAccessKey(), credentials.sessionToken());
  }

  public static StaticCredentialsProvider assumeRoleAndPersistProfile(
      StsClient stsClient,
      String roleArn,
      String roleSessionName,
      String mfaSerialNumber,
      String mfaTokenCode,
      String profile) {

    Objects.requireNonNull(profile, "profile must not be null");

    var credentials =
        assumeRoleCredentials(stsClient, roleArn, roleSessionName, mfaSerialNumber, mfaTokenCode);

    AwsCredentialsFileWriter.writeProfile(profile, credentials);

    return StaticCredentialsProvider.create(credentials);
  }
}
