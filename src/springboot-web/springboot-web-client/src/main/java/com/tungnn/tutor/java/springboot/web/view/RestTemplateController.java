package com.tungnn.tutor.java.springboot.web.view;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/rest-template")
public class RestTemplateController {

  private static final String API_URL = "http://localhost:8080/api";

  private final RestTemplate restTemplate;

  public RestTemplateController() {
    this.restTemplate = new RestTemplate();
  }

  // Methods for getForEntity which return ResponseEntity<T>
  @GetMapping("/users/{id}")
  public ResponseEntity<String> getUserEntity(@PathVariable Long id) {
    return restTemplate.getForEntity(API_URL + "/users/" + id, String.class);
  }

  @PostMapping("/users")
  public ResponseEntity<String> createUserEntity(@RequestBody String user) {
    return restTemplate.postForEntity(API_URL + "/users", user, String.class);
  }

  @PutMapping("/users/{id}")
  public void updateUserEntity(@PathVariable Long id, @RequestBody String user) {
    restTemplate.put(API_URL + "/users/" + id, user);
  }

  @DeleteMapping("/users/{id}")
  public void deleteUserEntity(@PathVariable Long id) {
    restTemplate.delete(API_URL + "/users/" + id);
  }

  // Methods for getForObject which return only the body of response
  @GetMapping("/users/object/{id}")
  public String getUser(@PathVariable Long id) {
    return restTemplate.getForObject(API_URL + "/users/" + id, String.class);
  }

  @PostMapping("/users/object")
  public String createUser(@RequestBody String user) {
    return restTemplate.postForObject(API_URL + "/users", user, String.class);
  }

  @PutMapping("/users/object/{id}")
  public void updateUser(@PathVariable Long id, @RequestBody String user) {
    restTemplate.put(API_URL + "/users/" + id, user);
  }

  @DeleteMapping("/users/object/{id}")
  public void deleteUser(@PathVariable Long id) {
    restTemplate.delete(API_URL + "/users/" + id);
  }

  // Methods for exchange() which for fully custom call API
  @GetMapping("/users/exchange/{id}")
  public ResponseEntity<String> getUserWithCustomHeader(@PathVariable Long id) {
    HttpHeaders headers = new HttpHeaders();
    headers.set("Custom-Header", "custom-value");

    HttpEntity<Void> requestEntity = new HttpEntity<>(headers);

    return restTemplate.exchange(
        API_URL + "/users/" + id, HttpMethod.GET, requestEntity, String.class);
  }
}
