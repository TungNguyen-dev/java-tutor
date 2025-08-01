package com.tungnn.tutor.java.springboot.web.view.spring_based;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/spring-annotation")
public class RequestParamController {

  @GetMapping("/request-param")
  public String requestParam(@RequestParam String name) {
    return "Request Param: " + name;
  }
}
