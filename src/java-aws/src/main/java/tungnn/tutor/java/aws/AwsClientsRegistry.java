package tungnn.tutor.java.aws;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.function.Supplier;
import software.amazon.awssdk.awscore.client.builder.AwsClientBuilder;
import software.amazon.awssdk.profiles.Profile;

public final class AwsClientsRegistry {

  private final ConcurrentMap<AwsClientCacheKey, Object> cache = new ConcurrentHashMap<>();
  private final AwsClientBuildersRegistry buildersRegistry;
  private final AwsClientsFactory clientsFactory;

  public AwsClientsRegistry(AwsClientsFactory clientsFactory) {
    this.clientsFactory = clientsFactory;
    this.buildersRegistry = new AwsClientBuildersRegistry();
  }

  public <C> void registerClient(
      Class<C> clientType, Supplier<? extends AwsClientBuilder<?, C>> builderSupplier) {

    buildersRegistry.register(clientType, builderSupplier);
  }

  @SuppressWarnings("unchecked")
  public <T> T getClient(Class<T> type, Profile profile) {
    var key = new AwsClientCacheKey(type, profile.name());
    return (T) cache.computeIfAbsent(key, k -> createClient(type, profile));
  }

  private <T> T createClient(Class<T> type, Profile profile) {
    var supplier = buildersRegistry.get(type);
    var builder = supplier.get();
    return clientsFactory.buildClient(builder, profile);
  }

  private record AwsClientCacheKey(Class<?> clientType, String profileName) {}

  private static class AwsClientBuildersRegistry {

    private final Map<Class<?>, Supplier<? extends AwsClientBuilder<?, ?>>> builders =
        new ConcurrentHashMap<>();

    public <C> void register(
        Class<C> clientType, Supplier<? extends AwsClientBuilder<?, C>> builderSupplier) {

      builders.put(clientType, builderSupplier);
    }

    @SuppressWarnings("unchecked")
    public <C> Supplier<? extends AwsClientBuilder<?, C>> get(Class<C> clientType) {

      var supplier = builders.get(clientType);

      if (supplier == null) {
        throw new IllegalStateException("No builder registered for " + clientType.getName());
      }

      return (Supplier<? extends AwsClientBuilder<?, C>>) supplier;
    }
  }
}
