package tungnn.tutor.java.pattern.architecture_pattern.t3_hexagonal.domain.port;

import tungnn.tutor.java.pattern.architecture_pattern.t3_hexagonal.domain.model.User;

public interface UserRepository {
  User save(User user);
}
