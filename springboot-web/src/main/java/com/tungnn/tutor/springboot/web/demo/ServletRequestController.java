package com.tungnn.tutor.springboot.web.demo;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ServletRequestController {

  @RequestMapping("/request-parameters")
  public String requestParameters(HttpServletRequest request) {
    return request.getParameter("name");
  }

  @RequestMapping("/request-headers")
  public String requestHeaders(HttpServletRequest request) {
    return request.getHeader("User-Agent");
  }
}
