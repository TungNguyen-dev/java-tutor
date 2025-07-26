package com.tungnn.tutor.springboot.web.demo2;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
public class UserController {

  /**
   * Creates a new user based on the provided user creation request.
   *
   * @param user the request body containing the details of the user to be created
   * @return a ResponseEntity containing the created User object
   */
  @PostMapping()
  public ResponseEntity<User> createUser(@RequestBody UserCreateRequest user) {
    return ResponseEntity.ok().body(new User(1, user.name(), user.email()));
  }
}
