package tungnn.tutor.java.pattern.architecture_pattern.t3_hexagonal.adapter.controller;

import tungnn.tutor.java.pattern.architecture_pattern.t3_hexagonal.domain.model.User;
import tungnn.tutor.java.pattern.architecture_pattern.t3_hexagonal.domain.port.UserService;

public class UserController {

  private final UserService userService;

  public UserController(UserService userService) {
    this.userService = userService;
  }

  public User createUser(String name) {
    return userService.createUser(name);
  }
}
