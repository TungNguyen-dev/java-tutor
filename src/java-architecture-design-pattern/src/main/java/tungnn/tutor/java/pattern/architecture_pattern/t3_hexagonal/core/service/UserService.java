package tungnn.tutor.java.pattern.architecture_pattern.t3_hexagonal.domain.port;

import tungnn.tutor.java.pattern.architecture_pattern.t3_hexagonal.domain.model.User;

public interface UserService {
  User createUser(String name);
}
