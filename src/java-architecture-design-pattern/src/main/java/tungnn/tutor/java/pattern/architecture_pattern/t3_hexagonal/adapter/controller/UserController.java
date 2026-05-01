package tungnn.tutor.java.pattern.architecture_pattern.t3_hexagonal.adapter.controller;

import tungnn.tutor.java.pattern.architecture_pattern.t3_hexagonal.domain.model.User;
import tungnn.tutor.java.pattern.architecture_pattern.t3_hexagonal.domain.port.usecase.CreateUserUseCase;

public class UserController {

  private final CreateUserUseCase createUserUseCase;

  public UserController(CreateUserUseCase createUserUseCase) {
    this.createUserUseCase = createUserUseCase;
  }

  public User createUser(String name) {
    return createUserUseCase.createUser(name);
  }
}
