package com.tungnn.tutor.spring.context;

import com.tungnn.tutor.spring.context.beans.MyService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class XmlApplicationContextLoader {

  public static void main(String[] args) {
    ApplicationContext context = new ClassPathXmlApplicationContext("application-beans.xml");
    MyService myService = context.getBean("myService", MyService.class);
    myService.sayHello();
  }
}
