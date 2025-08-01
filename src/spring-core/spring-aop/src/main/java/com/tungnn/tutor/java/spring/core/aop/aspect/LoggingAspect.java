package com.tungnn.tutor.java.spring.core.aop.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LoggingAspect {

  @Before("execution(* com.tungnn.tutor.java.spring.core.aop.service.MyService.*(..))")
  public void logBefore() {
    System.out.println("[@Before] Before method execution");
  }

  @AfterReturning(
      pointcut = "execution(* com.tungnn.tutor.java.spring.core.aop.service.MyService.*(..))",
      returning = "result")
  public void logReturn(Object result) {
    System.out.println("[@AfterReturning] Method returned: " + result);
  }

  @AfterThrowing(
      pointcut = "execution(* com.tungnn.tutor.java.spring.core.aop.service.MyService.*(..))",
      throwing = "exception")
  public void logException(Exception exception) {
    System.out.println("[@AfterThrowing] Exception caught: " + exception.getMessage());
  }

  @After("execution(* com.tungnn.tutor.java.spring.core.aop.service.MyService.*(..))")
  public void logAfter() {
    System.out.println("[@After] After method execution");
  }

  @Around("execution(* com.tungnn.tutor.java.spring.core.aop.service.MyService.*(..))")
  public Object logExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {
    long start = System.currentTimeMillis();
    Object proceed = joinPoint.proceed();
    long executionTime = System.currentTimeMillis() - start;
    System.out.println("[@Around] Method executed in " + executionTime + "ms");
    return proceed;
  }
}
