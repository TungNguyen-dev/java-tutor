package tungnn.tutor.java.pattern.architecture_pattern.t3_hexagonal.adapter.persistence;

import tungnn.tutor.java.pattern.architecture_pattern.t3_hexagonal.domain.model.User;
import tungnn.tutor.java.pattern.architecture_pattern.t3_hexagonal.domain.port.UserRepository;

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
