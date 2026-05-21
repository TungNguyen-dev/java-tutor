package tungnn.tutor.java.architecture_pattern.t3_hexagonal.adapter.outbound;

import tungnn.tutor.java.architecture_pattern.t3_hexagonal.core.domain.entity.User;
import tungnn.tutor.java.architecture_pattern.t3_hexagonal.core.port.outbound.UserRepository;

import java.util.HashMap;
import java.util.Map;

public class InMemoryUserRepository implements UserRepository {

  private final Map<String, User> database = new HashMap<>();

  @Override
  public User save(User user) {
    database.put(user.id(), user);
    return user;
  }
}
