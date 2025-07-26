package com.tungnn.tutor.springboot.web.demo4;

import java.util.List;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class HomeController {

  @GetMapping("/home")
  public String home() {
    return "home";
  }

  @GetMapping("/users")
  public String getAllUsers(Model model) {
    model.addAttribute("users", List.of("user1", "user2", "user3"));
    return "user-list";
  }

  @GetMapping("/users/all")
  public ModelAndView getAllUsers() {
    ModelAndView modelAndView = new ModelAndView("user-list");
    modelAndView.addObject("users", List.of("user1", "user2", "user3"));
    return modelAndView;
  }
}
