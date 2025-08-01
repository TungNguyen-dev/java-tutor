package com.tungnn.tutor.java.spring.context.annotation;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan("com.tungnn.tutor.spring.context.demo.annotation")
public class Application {

  public static void main(String[] args) {
    ApplicationContext context = new AnnotationConfigApplicationContext(Application.class);

    MyService myService = context.getBean("myServiceImpl", MyService.class);
    myService.sayHello();
  }
}
