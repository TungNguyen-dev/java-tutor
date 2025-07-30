package com.tungnn.tutor.java.springboot.web.view.java_servlet;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/servlet-request")
public class ServletRequestController {

  @GetMapping("/parameters")
  public String requestParameters(HttpServletRequest request) {
    return request.getParameter("name");
  }
}
