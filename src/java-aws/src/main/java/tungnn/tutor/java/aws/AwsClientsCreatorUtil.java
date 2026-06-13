package tungnn.tutor.java.aws;

import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider;
import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.auth.credentials.ProfileCredentialsProvider;
import software.amazon.awssdk.awscore.client.builder.AwsClientBuilder;
import software.amazon.awssdk.core.client.config.ClientOverrideConfiguration;
import software.amazon.awssdk.core.client.config.SdkAdvancedClientOption;
import software.amazon.awssdk.core.retry.RetryMode;
import software.amazon.awssdk.metrics.LoggingMetricPublisher;
import software.amazon.awssdk.regions.Region;

import java.time.Duration;

public final class AwsClientsCreatorUtil {

  private AwsClientsCreatorUtil() {}

  // =========================
  // Environment-driven config
  // =========================

  private static final boolean ENABLE_METRICS =
      Boolean.parseBoolean(System.getenv().getOrDefault("AWS_SDK_ENABLE_METRICS", "false"));

  private static final Region DEFAULT_REGION =
      Region.of(System.getenv().getOrDefault("AWS_REGION", "ap-southeast-1"));

  private static final String USER_AGENT_PREFIX =
      System.getenv().getOrDefault("AWS_SDK_USER_AGENT_PREFIX", "env/dev");

  // =========================
  // Credentials Provider
  // =========================

  private static AwsCredentialsProvider credentialsProvider() {
    String profile = System.getenv("AWS_PROFILE");

    if (profile != null && !profile.isBlank()) {
      return ProfileCredentialsProvider.create(profile);
    }

    return DefaultCredentialsProvider.create();
  }

  // =========================
  // Shared override config
  // =========================

  private static ClientOverrideConfiguration overrideConfig() {
    ClientOverrideConfiguration.Builder builder =
        ClientOverrideConfiguration.builder()
            // Timeout strategy
            .apiCallTimeout(Duration.ofSeconds(30))
            .apiCallAttemptTimeout(Duration.ofSeconds(10))

            // Retry strategy (adaptive)
            .retryPolicy(RetryMode.ADAPTIVE)

            // User-Agent (important for tracking/debug)
            .putAdvancedOption(SdkAdvancedClientOption.USER_AGENT_PREFIX, USER_AGENT_PREFIX);

    // Optional metrics
    if (ENABLE_METRICS) {
      builder.addMetricPublisher(LoggingMetricPublisher.create());
    }

    return builder.build();
  }

  // =========================
  // Generic client factory
  // =========================

  public static <B extends AwsClientBuilder<B, C>, C> C createClient(B builder) {
    return builder
        .region(DEFAULT_REGION)
        .credentialsProvider(credentialsProvider())
        .overrideConfiguration(overrideConfig())
        .build();
  }
}
