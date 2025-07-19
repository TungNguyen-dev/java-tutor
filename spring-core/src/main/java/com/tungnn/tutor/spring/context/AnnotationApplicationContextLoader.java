package com.tungnn.tutor.spring.context;

import com.tungnn.tutor.spring.context.beans.ExampleService;
import com.tungnn.tutor.spring.context.beans.MyService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan(basePackages = "com.tungnn.tutor.spring.context")
public class AnnotationApplicationContextLoader {

  public AnnotationApplicationContextLoader(
      @Qualifier("exampleServiceImpl2") ExampleService exampleService) {
    System.out.println("AnnotationApplicationContextLoader constructor");
  }

  public static void main(String[] args) {
    ApplicationContext context =
        new AnnotationConfigApplicationContext(AnnotationApplicationContextLoader.class);

    MyService myService = context.getBean(MyService.class);
    myService.sayHello();
  }
}
