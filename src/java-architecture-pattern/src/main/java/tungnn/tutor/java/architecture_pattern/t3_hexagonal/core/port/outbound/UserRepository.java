package tungnn.tutor.java.architecture_pattern.t3_hexagonal.core.port.outbound;

import tungnn.tutor.java.architecture_pattern.t3_hexagonal.core.domain.entity.User;

public interface UserRepository {
  User save(User user);
}
