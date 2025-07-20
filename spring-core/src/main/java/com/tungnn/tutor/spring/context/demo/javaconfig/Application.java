package com.tungnn.tutor.spring.context.demo.javaconfig;

import org.springframework.context.support.GenericApplicationContext;

public class Application {

  public static void main(String[] args) {
    GenericApplicationContext context = new GenericApplicationContext();

    // Manually register a bean
    context.registerBean("myServiceImpl", MyService.class, MyServiceImpl::new);
    context.registerBean("myServiceImpl2", MyService.class, MyServiceImpl2::new);

    // Refresh context to initialize beans
    context.refresh();

    // Retrieve and use the bean
    MyService service = context.getBean("myServiceImpl", MyService.class);
    service.sayHello();
  }
}
