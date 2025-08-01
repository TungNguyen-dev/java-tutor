package com.tungnn.tutor.java.springboot.web.view.spring_based;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/spring-annotation")
public class RequestBodyController {

  @PostMapping("/request-body")
  public RequestBodyDTO requestBody(@RequestBody RequestBodyDTO requestBody) {
    return requestBody;
  }

  public record RequestBodyDTO(String name, String email) {}
}
