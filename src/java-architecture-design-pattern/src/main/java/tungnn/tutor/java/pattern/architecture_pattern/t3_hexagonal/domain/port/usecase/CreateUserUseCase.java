package tungnn.tutor.java.pattern.architecture_pattern.t3_hexagonal.domain.port.usecase;

import tungnn.tutor.java.pattern.architecture_pattern.t3_hexagonal.domain.model.User;

public interface CreateUserUseCase {
  User createUser(String name);
}
