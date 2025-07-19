package com.tungnn.tutor.spring.context.beans;

import org.springframework.stereotype.Component;

@Component
public final class MyService {

  public void sayHello() {
    System.out.println("Hello, World!");
  }
}
