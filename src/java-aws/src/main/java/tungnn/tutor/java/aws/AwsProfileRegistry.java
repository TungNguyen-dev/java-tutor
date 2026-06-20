package tungnn.tutor.java.aws;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import software.amazon.awssdk.profiles.Profile;
import software.amazon.awssdk.profiles.ProfileFile;

public final class AwsProfileRegistry {

  private final Map<String, Profile> profiles = new ConcurrentHashMap<>();
  private final ProfileFile profileFile;

  public AwsProfileRegistry() {
    this(ProfileFile.defaultProfileFile());
  }

  public AwsProfileRegistry(ProfileFile profileFile) {
    this.profileFile = Objects.requireNonNull(profileFile);
    loadAllProfiles();
  }

  private void loadAllProfiles() {
    profiles.putAll(profileFile.profiles());
  }

  public Profile profile(String profileName) {
    var profile = profiles.get(profileName);

    if (profile == null) {
      throw new IllegalArgumentException("Profile not found: " + profileName);
    }

    return profile;
  }
}
