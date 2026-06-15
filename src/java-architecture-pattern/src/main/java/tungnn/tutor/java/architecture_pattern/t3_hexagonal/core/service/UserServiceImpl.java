package tungnn.tutor.java.architecture_pattern.t3_hexagonal.core.service;

import java.util.UUID;
import tungnn.tutor.java.architecture_pattern.t3_hexagonal.core.domain.entity.User;
import tungnn.tutor.java.architecture_pattern.t3_hexagonal.core.port.outbound.UserRepository;

public class UserServiceImpl implements UserService {

  private final UserRepository userRepository;

  public UserServiceImpl(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  @Override
  public User createUser(String name) {
    User user = new User(UUID.randomUUID().toString(), name);
    return userRepository.save(user);
  }
}
