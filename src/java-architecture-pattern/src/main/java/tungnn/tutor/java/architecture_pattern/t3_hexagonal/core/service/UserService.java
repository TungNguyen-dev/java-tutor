package tungnn.tutor.java.architecture_pattern.t3_hexagonal.core.service;

import tungnn.tutor.java.architecture_pattern.t3_hexagonal.core.domain.entity.User;

public interface UserService {
  User createUser(String name);
}
