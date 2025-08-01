package com.tungnn.tutor.java.spring.context.annotation;

import com.tungnn.tutor.java.spring.context.annotation.service.CustomService;
import com.tungnn.tutor.java.spring.context.annotation.service.MyService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan
public class ContextAnnotationBasedApplication {

  public static void main(String[] args) {
    ApplicationContext context = new AnnotationConfigApplicationContext(ContextAnnotationBasedApplication.class);

    MyService myService = context.getBean("myServiceImpl", MyService.class);
    myService.sayHello();

    CustomService customService = context.getBean("customService", CustomService.class);
    customService.sayHello();
  }
}
