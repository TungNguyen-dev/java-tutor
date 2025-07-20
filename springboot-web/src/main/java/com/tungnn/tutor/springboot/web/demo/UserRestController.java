package com.tungnn.tutor.springboot.web.demo;

import java.util.List;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
public class UserRestController {

  public record User(Integer userId, String name, String email) {}

  @GetMapping
  public List<User> getUsers() {
    return List.of(new User(1, "Tung", "<EMAIL>"));
  }

  @GetMapping("/{userId}")
  public User getUserById(@PathVariable String userId) {
    return new User(Integer.valueOf(userId), "Tung", "<EMAIL>");
  }

  @PostMapping
  public User createUser(@RequestBody User user) {
    return user;
  }

  @PutMapping("/{userId}")
  public User updateUser(@PathVariable String userId, @RequestBody User user) {
    return user;
  }

  @DeleteMapping("/{userId}")
  public void deleteUser(@PathVariable String userId) {
    System.out.println("Delete user with id: " + userId);
  }
}
