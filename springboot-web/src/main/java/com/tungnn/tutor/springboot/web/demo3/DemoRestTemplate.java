package com.tungnn.tutor.springboot.web.demo3;

import java.util.List;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

public class DemoRestTemplate {

  private static final String GET_USERS_URL = "https://api.example.com/users";
  private static final String CREATE_USER_URL = "https://api.example.com/users";
  private static final String UPDATE_USER_URL = "https://api.example.com/users/{id}";
  private static final String DELETE_USER_URL = "https://api.example.com/users/{id}";

  private final RestTemplate restTemplate;

  public DemoRestTemplate() {
    this.restTemplate = new RestTemplate();
  }

  public User[] getAllUsers() {
    return restTemplate.getForObject(GET_USERS_URL, User[].class);
  }

  public User createUser(User user) {
    return restTemplate.postForObject(CREATE_USER_URL, user, User.class);
  }

  public void updateUser(Long id, User user) {
    restTemplate.put(UPDATE_USER_URL, user, id);
  }

  public void deleteUser(Long id) {
    restTemplate.delete(DELETE_USER_URL, id);
  }

  /**
   * Retrieves a response containing a User object from the specified URL using the RestTemplate.
   *
   * @return a ResponseEntity containing the User object retrieved from the get-go request.
   */
  public ResponseEntity<User> getAllForEntity() {
    return restTemplate.getForEntity(GET_USERS_URL, User.class);
  }

  public ResponseEntity<User> exchangeExample(Long id) {
    HttpHeaders headers = new HttpHeaders();
    headers.set("Custom-Header", "value");

    HttpEntity<Void> requestEntity = new HttpEntity<>(headers);

    return restTemplate.exchange(
        GET_USERS_URL + "/{id}", HttpMethod.GET, requestEntity, User.class, id);
  }

  public ResponseEntity<List<User>> exchangeGetAllUsers() {
    HttpHeaders headers = new HttpHeaders();
    headers.set("Custom-Header", "value");

    HttpEntity<Void> requestEntity = new HttpEntity<>(headers);

    return restTemplate.exchange(
        GET_USERS_URL, HttpMethod.GET, requestEntity, new ParameterizedTypeReference<>() {});
  }
}
