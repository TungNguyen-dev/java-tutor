package com.tungnn.tutor.java.spring.context.xml;

import com.tungnn.tutor.java.spring.context.xml.service.MyService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class ContextXMLBasedApplication {

  public static void main(String[] args) {
    ApplicationContext context = new ClassPathXmlApplicationContext("application-beans.xml");
    MyService myService = context.getBean("myService", MyService.class);
    myService.sayHello();
  }
}
