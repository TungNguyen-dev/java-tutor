package com.tungnn.tutor.java.spring.context.annotation.service.impl;

import com.tungnn.tutor.java.spring.context.annotation.service.CustomService;

public class CustomServiceImpl implements CustomService {

  @Override
  public void sayHello() {
    System.out.println("CustomServiceImpl: Hello World!");
  }
}
