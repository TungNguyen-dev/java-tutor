package com.tungnn.tutor.java.springboot.data.jpa;

import com.tungnn.tutor.java.springboot.data.jpa.service.EmployeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@RequiredArgsConstructor
public class JpaDataApplication implements CommandLineRunner {

  private final EmployeeService employeeService;

  public static void main(String[] args) {
    SpringApplication.run(JpaDataApplication.class, args);
  }

  @Override
  public void run(String... args) throws Exception {
    employeeService.getAllEmployees().forEach(System.out::println);
  }
}
