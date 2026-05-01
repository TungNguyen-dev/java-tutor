package tungnn.tutor.java.pattern.architecture_pattern.t3_hexagonal.domain.service;

import tungnn.tutor.java.pattern.architecture_pattern.t3_hexagonal.domain.model.User;
import tungnn.tutor.java.pattern.architecture_pattern.t3_hexagonal.domain.port.persistence.UserRepository;
import tungnn.tutor.java.pattern.architecture_pattern.t3_hexagonal.domain.port.usecase.CreateUserUseCase;

import java.util.UUID;

public class CreateUserService implements CreateUserUseCase {

  private final UserRepository userRepository;

  public CreateUserService(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  @Override
  public User createUser(String name) {
    User user = new User(UUID.randomUUID().toString(), name);
    return userRepository.save(user);
  }
}
