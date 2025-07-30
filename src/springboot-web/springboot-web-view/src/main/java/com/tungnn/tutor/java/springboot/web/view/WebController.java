package com.tungnn.tutor.java.springboot.web.view;

import java.util.List;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class WebController {

  // String name of view
  @GetMapping("/home")
  public String home() {
    return "home";
  }

  // Model to add data to HTML
  @GetMapping("/users")
  public String getAllUsers(Model model) {
    model.addAttribute("users", List.of("user1", "user2", "user3"));
    return "user-list";
  }

  // ModelAndView for convenience
  @GetMapping("/users/all")
  public ModelAndView getAllUsers() {
    ModelAndView modelAndView = new ModelAndView("user-list");
    modelAndView.addObject("users", List.of("user1", "user2", "user3"));
    return modelAndView;
  }
}
