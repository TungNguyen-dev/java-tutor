package tungnn.tutor.java.architecture_pattern.t3_hexagonal.adapter.inbound;

import tungnn.tutor.java.architecture_pattern.t3_hexagonal.core.domain.entity.User;
import tungnn.tutor.java.architecture_pattern.t3_hexagonal.core.service.UserService;

public class UserController {

  private final UserService userService;

  public UserController(UserService userService) {
    this.userService = userService;
  }

  public User createUser(String name) {
    return userService.createUser(name);
  }
}
