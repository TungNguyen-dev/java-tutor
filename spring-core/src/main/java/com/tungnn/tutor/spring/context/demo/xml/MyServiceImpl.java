package com.tungnn.tutor.spring.context.demo.xml;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
@Qualifier("myServiceImpl")
public class MyServiceImpl implements MyService {

  @Override
  public void sayHello() {
    System.out.println("MyServiceImpl: Hello World!");
  }
}
