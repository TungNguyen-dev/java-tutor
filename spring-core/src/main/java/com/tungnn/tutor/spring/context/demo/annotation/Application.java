package com.tungnn.tutor.spring.context.demo.annotation;

import com.tungnn.tutor.spring.context.demo.xml.MyService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class Application {

  public static void main(String[] args) {
    ApplicationContext context = new AnnotationConfigApplicationContext(Application.class);

    MyService myService = context.getBean("myServiceImpl", MyService.class);
    myService.sayHello();
  }
}
