package tungnn.tutor.java.aws;

import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider;
import software.amazon.awssdk.auth.credentials.ProfileCredentialsProvider;
import software.amazon.awssdk.profiles.Profile;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.sts.StsClient;
import tungnn.tutor.java.aws.service.sts.StsUtil;

public class AwsCredentialsProviderFactory {

  private final ConcurrentMap<String, StsClient> stsClientsCache = new ConcurrentHashMap<>();

  public AwsCredentialsProvider resolve(Profile profile) {
    Objects.requireNonNull(profile, "profile must not be null");

    var roleArn = profile.property("role_arn").orElse(null);
    if (roleArn != null) {
      return assumeRoleWithRefresh(profile, roleArn);
    }

    return ProfileCredentialsProvider.create(profile.name());
  }

  private AwsCredentialsProvider assumeRoleWithRefresh(Profile profile, String roleArn) {
    var region = Region.of(profile.property("region").orElse("ap-southeast-1"));

    var stsClient =
        stsClientsCache.computeIfAbsent(
            profile.name(),
            key ->
                StsClient.builder()
                    .credentialsProvider(
                        ProfileCredentialsProvider.builder().profileName(profile.name()).build())
                    .region(region)
                    .build());

    return StsUtil.createAssumeRoleCredentialsProvider(stsClient, roleArn);
  }
}
