package com.tungnn.tutor.spring.context.demo.configuration_class;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Application {

  public static void main(String[] args) {
    ApplicationContext context = new AnnotationConfigApplicationContext(Application.class);

    MyService myService = context.getBean("myServiceImpl", MyService.class);
    myService.sayHello();
  }

  @Bean
  public MyService myServiceImpl() {
    return new MyServiceImpl();
  }

  @Bean
  public MyService myServiceImpl2() {
    return new MyServiceImpl2();
  }
}
