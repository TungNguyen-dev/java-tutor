package com.tungnn.tutor.java.springboot.web.view.spring_based;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/spring-annotation")
public class RequestHeaderController {

  @GetMapping("/request-header")
  public String requestHeader(@RequestHeader("User-Agent") String agent) {
    return "User-Agent: " + agent;
  }
}
