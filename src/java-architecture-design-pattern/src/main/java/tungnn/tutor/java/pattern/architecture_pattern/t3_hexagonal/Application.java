package tungnn.tutor.java.pattern.architecture_pattern.t3_hexagonal;

import tungnn.tutor.java.pattern.architecture_pattern.t3_hexagonal.adapter.controller.UserController;
import tungnn.tutor.java.pattern.architecture_pattern.t3_hexagonal.adapter.persistence.InMemoryUserRepository;
import tungnn.tutor.java.pattern.architecture_pattern.t3_hexagonal.domain.port.persistence.UserRepository;
import tungnn.tutor.java.pattern.architecture_pattern.t3_hexagonal.domain.service.CreateUserService;

public class Application {

  static void main() {
    // Adapter (out)
    UserRepository userRepository = new InMemoryUserRepository();

    // Core (use case)
    CreateUserService createUserService = new CreateUserService(userRepository);

    // Adapter (in)
    UserController controller = new UserController(createUserService);

    // Simulate request
    var user = controller.createUser("TungNN");

    System.out.println("Created user: " + user.name());
  }
}
