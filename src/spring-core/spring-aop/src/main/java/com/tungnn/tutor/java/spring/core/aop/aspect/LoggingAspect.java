package com.tungnn.tutor.java.spring.core.aop.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LoggingAspect {

  @Before("execution(* com.tungnn.tutor.java.spring.core.aop.service.MyService.*(..))")
  public void logDetailsBefore(JoinPoint joinPoint) {
    // Gets method name
    String methodName = joinPoint.getSignature().getName();
    // Gets method arguments
    Object[] args = joinPoint.getArgs();

    System.out.println("[@Before] Method: " + methodName + " will execute");
    if (args.length > 0) {
      System.out.println("[@Before] Arguments: ");
      for (Object arg : args) {
        System.out.println(" - " + arg);
      }
    } else {
      System.out.println("[@Before] No arguments passed");
    }
  }

  @AfterReturning(
      pointcut = "execution(* com.tungnn.tutor.java.spring.core.aop.service.MyService.*(..))",
      returning = "result")
  public void logDetailsAfterReturning(JoinPoint joinPoint, Object result) {
    // Gets method name
    String methodName = joinPoint.getSignature().getName();
    System.out.println("[@AfterReturning] Method: " + methodName + " executed successfully");

    // Log the return value
    if (result != null) {
      System.out.println("[@AfterReturning] Returned value: " + result);
    } else {
      System.out.println("[@AfterReturning] Method returned void or null");
    }
  }

  @AfterThrowing(
      pointcut = "execution(* com.tungnn.tutor.java.spring.core.aop.service.MyService.*(..))",
      throwing = "exception")
  public void logDetailsAfterThrowing(JoinPoint joinPoint, Exception exception) {
    // Gets method name
    String methodName = joinPoint.getSignature().getName();
    System.out.println("[@AfterThrowing] Method: " + methodName + " threw an exception");
    System.out.println("[@AfterThrowing] Exception Message: " + exception.getMessage());
  }

  @After("execution(* com.tungnn.tutor.java.spring.core.aop.service.MyService.*(..))")
  public void logDetailsAfter(JoinPoint joinPoint) {
    // Gets method name
    String methodName = joinPoint.getSignature().getName();
    System.out.println("[@After] Method: " + methodName + " has finished execution");
  }
}
