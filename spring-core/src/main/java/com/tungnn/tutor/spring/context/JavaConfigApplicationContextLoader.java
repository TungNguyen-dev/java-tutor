package com.tungnn.tutor.spring.context;

import com.tungnn.tutor.spring.context.beans.MyService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JavaConfigApplicationContextLoader {

  public static void main(String[] args) {
    ApplicationContext context =
        new AnnotationConfigApplicationContext(JavaConfigApplicationContextLoader.class);

    MyService myService = context.getBean(MyService.class);
    myService.sayHello();
  }

  @Bean
  public MyService myService() {
    return new MyService();
  }
}
