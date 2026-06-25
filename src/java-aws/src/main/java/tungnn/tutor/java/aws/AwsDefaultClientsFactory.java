package tungnn.tutor.java.aws;

import java.time.Duration;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import software.amazon.awssdk.awscore.AwsClient;
import software.amazon.awssdk.awscore.client.builder.AwsClientBuilder;
import software.amazon.awssdk.core.client.config.ClientOverrideConfiguration;
import software.amazon.awssdk.core.client.config.SdkAdvancedClientOption;
import software.amazon.awssdk.core.retry.RetryMode;
import software.amazon.awssdk.metrics.LoggingMetricPublisher;
import software.amazon.awssdk.profiles.Profile;
import software.amazon.awssdk.regions.Region;
import tungnn.tutor.java.aws.util.AwsCredentialsProviderUtil;

public final class AwsDefaultClientsFactory implements AwsClientsFactory {

  private static final String REGION_PROPERTY = "region";
  private static final String DEFAULT_REGION = "ap-southeast-1";

  private static final Duration API_CALL_TIMEOUT = Duration.ofSeconds(30);
  private static final Duration API_CALL_ATTEMPT_TIMEOUT = Duration.ofSeconds(10);

  private static final boolean METRICS_ENABLED =
      Boolean.parseBoolean(System.getenv("AWS_SDK_ENABLE_METRICS"));

  private final ConcurrentMap<ClientCacheKey, AwsClient> cache = new ConcurrentHashMap<>();

  @Override
  @SuppressWarnings("unchecked")
  public <C extends AwsClient> C getClient(
      Class<C> clientType, AwsClientBuilder<?, C> builder, Profile profile) {

    Objects.requireNonNull(clientType, "clientType must not be null");
    Objects.requireNonNull(builder, "builder must not be null");
    Objects.requireNonNull(profile, "profile must not be null");

    var key = new ClientCacheKey(clientType, profile.name());

    return (C) cache.computeIfAbsent(key, ignored -> createClient(builder, profile));
  }

  @Override
  public void clearCache() {
    cache.clear();
  }

  private <C extends AwsClient> C createClient(AwsClientBuilder<?, C> builder, Profile profile) {
    var region = Region.of(profile.property(REGION_PROPERTY).orElse(DEFAULT_REGION));

    return builder
        .region(region)
        .credentialsProvider(AwsCredentialsProviderUtil.credentialsProvider(profile))
        .overrideConfiguration(createOverrideConfiguration(profile))
        .build();
  }

  private ClientOverrideConfiguration createOverrideConfiguration(Profile profile) {
    var builder =
        ClientOverrideConfiguration.builder()
            .apiCallTimeout(API_CALL_TIMEOUT)
            .apiCallAttemptTimeout(API_CALL_ATTEMPT_TIMEOUT)
            .retryStrategy(RetryMode.ADAPTIVE_V2)
            .putAdvancedOption(
                SdkAdvancedClientOption.USER_AGENT_PREFIX, "env-%s".formatted(profile.name()));

    if (METRICS_ENABLED) {
      builder.addMetricPublisher(LoggingMetricPublisher.create());
    }

    return builder.build();
  }

  private record ClientCacheKey(Class<?> clientType, String profileName) {}
}
