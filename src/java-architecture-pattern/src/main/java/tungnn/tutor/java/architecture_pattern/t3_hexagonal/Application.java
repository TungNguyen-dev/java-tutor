package tungnn.tutor.java.architecture_pattern.t3_hexagonal;

import tungnn.tutor.java.architecture_pattern.t3_hexagonal.adapter.inbound.UserController;
import tungnn.tutor.java.architecture_pattern.t3_hexagonal.adapter.outbound.InMemoryUserRepository;
import tungnn.tutor.java.architecture_pattern.t3_hexagonal.core.port.outbound.UserRepository;
import tungnn.tutor.java.architecture_pattern.t3_hexagonal.core.service.UserService;
import tungnn.tutor.java.architecture_pattern.t3_hexagonal.core.service.UserServiceImpl;

public class Application {

  static void main() {
    // Adapter (out)
    UserRepository userRepository = new InMemoryUserRepository();

    // Core (use case)
    UserService createUserService = new UserServiceImpl(userRepository);

    // Adapter (in)
    UserController controller = new UserController(createUserService);

    // Simulate request
    var user = controller.createUser("TungNN");

    System.out.println("Created user: " + user.name());
  }
}
