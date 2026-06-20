package tungnn.tutor.java.aws;

import java.time.Duration;
import software.amazon.awssdk.awscore.client.builder.AwsClientBuilder;
import software.amazon.awssdk.core.client.config.ClientOverrideConfiguration;
import software.amazon.awssdk.core.client.config.SdkAdvancedClientOption;
import software.amazon.awssdk.core.retry.RetryMode;
import software.amazon.awssdk.metrics.LoggingMetricPublisher;
import software.amazon.awssdk.profiles.Profile;
import software.amazon.awssdk.profiles.ProfileFile;
import software.amazon.awssdk.regions.Region;

public final class AwsClientsFactory {

  private final AwsCredentialsProviderFactory credentialsFactory =
      new AwsCredentialsProviderFactory();

  private static Profile defaultProfile() {
    return DefaultProfileHolder.INSTANCE;
  }

  public <C> C buildClient(AwsClientBuilder<?, C> builder) {
    return buildClient(builder, defaultProfile());
  }

  public <C> C buildClient(AwsClientBuilder<?, C> builder, Profile profile) {
    var region = Region.of(profile.property("region").orElse("ap-southeast-1"));

    return builder
        .region(region)
        .credentialsProvider(credentialsFactory.resolve(profile))
        .overrideConfiguration(createOverrideConfig(profile))
        .build();
  }

  private ClientOverrideConfiguration createOverrideConfig(Profile profile) {
    var builder =
        ClientOverrideConfiguration.builder()
            .apiCallTimeout(Duration.ofSeconds(30))
            .apiCallAttemptTimeout(Duration.ofSeconds(10))
            .retryStrategy(RetryMode.ADAPTIVE_V2)
            .putAdvancedOption(
                SdkAdvancedClientOption.USER_AGENT_PREFIX, "env-%s".formatted(profile.name()));

    if (profile.booleanProperty("enable_metrics").orElse(false)) {
      builder.addMetricPublisher(LoggingMetricPublisher.create());
    }

    return builder.build();
  }

  private static class DefaultProfileHolder {
    private static final Profile INSTANCE = loadDefaultProfile();

    private static Profile loadDefaultProfile() {
      var profileName = System.getenv().getOrDefault("AWS_PROFILE", "default");
      return ProfileFile.defaultProfileFile()
          .profile(profileName)
          .orElseThrow(() -> new RuntimeException("Profile not found: " + profileName));
    }
  }
}
