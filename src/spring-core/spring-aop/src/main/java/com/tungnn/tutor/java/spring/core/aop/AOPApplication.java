package com.tungnn.tutor.java.spring.core.aop;

import com.tungnn.tutor.java.spring.core.aop.service.MyService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@EnableAspectJAutoProxy
@ComponentScan("com.tungnn.tutor.java.spring.core.aop")
public class AOPApplication {

  public static void main(String[] args) {
    ApplicationContext context = new AnnotationConfigApplicationContext(AOPApplication.class);
    MyService myService = context.getBean("myService", MyService.class);
    myService.doWork();
  }
}
