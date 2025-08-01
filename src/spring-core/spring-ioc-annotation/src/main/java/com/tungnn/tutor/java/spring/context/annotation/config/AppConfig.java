package com.tungnn.tutor.java.spring.context.annotation.config;

import com.tungnn.tutor.java.spring.context.annotation.service.CustomService;
import com.tungnn.tutor.java.spring.context.annotation.service.impl.CustomServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/** Java class configuration */
@Configuration
public class AppConfig {

  @Bean
  public CustomService customService() {
    return new CustomServiceImpl();
  }
}
