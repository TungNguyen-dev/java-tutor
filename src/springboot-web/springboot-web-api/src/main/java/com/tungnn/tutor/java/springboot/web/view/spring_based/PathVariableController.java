package com.tungnn.tutor.java.springboot.web.view.spring_based;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/spring-annotation")
public class PathVariableController {

  @GetMapping("/path-variable/{requestId}")
  public String index(@PathVariable String requestId) {
    return "Path Variable: " + requestId;
  }
}
