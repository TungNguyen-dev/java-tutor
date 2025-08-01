package com.tungnn.tutor.java.spring.context.xml.service.impl;

import com.tungnn.tutor.java.spring.context.xml.service.MyService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
@Qualifier("myServiceImpl2")
public class MyServiceImpl2 implements MyService {

  @Override
  public void sayHello() {
    System.out.println("MyServiceImpl2: Hello World!");
  }
}
