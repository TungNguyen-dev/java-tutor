package com.tungnn.tutor.spring.context;

import com.tungnn.tutor.spring.context.beans.MyService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan(basePackages = "com.tungnn.tutor.spring.context")
public class AnnotationApplicationContextLoader {

  public static void main(String[] args) {
    ApplicationContext context =
        new AnnotationConfigApplicationContext(AnnotationApplicationContextLoader.class);

    MyService myService = context.getBean("myServiceImpl", MyService.class);
    myService.sayHello();
  }
}
