package com.tungnn.tutor.spring.batch.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SpringBatchDemoApplication {

  public static void main(String[] args) {
    System.exit(
        SpringApplication.exit(SpringApplication.run(SpringBatchDemoApplication.class, args)));
  }
}
