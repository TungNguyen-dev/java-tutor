package tungnn.tutor.java.architecture_pattern.t3_hexagonal.core.port.inbound;

import tungnn.tutor.java.pattern.architecture_pattern.t3_hexagonal.core.domain.entity.User;

public interface UserController {
  User createUser(Object obj);
}
