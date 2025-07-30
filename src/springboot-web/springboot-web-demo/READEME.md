# springboot-web

## Overview

This is a Spring Boot web application project that demonstrates the implementation of a web service
using a Spring Boot framework. The project serves as a template for building web applications with
Spring Boot.

## Technology Stack

- Java 24
- Spring Boot 3.5.4
- Spring MVC
- Maven

## Prerequisites

- JDK 24 or later
- Maven 3.6.x or later
- Your favorite IDE (IntelliJ IDEA, Eclipse, VS Code)

## Getting Started

## Spring Boot Annotations

Common Spring Boot annotations are used in this project:

- `@SpringBootApplication`: Main application annotation that combines `@Configuration`,
  `@EnableAutoConfiguration`, and `@ComponentScan`
- `@Controller`: Marks a class as Spring MVC controller
- `@RestController`: Specialized version of `@Controller` for RESTful services
- `@RequestMapping`: Maps HTTP requests to handler methods
- `@Autowired`: Enables dependency injection

## Jakarta Servlets

Jakarta Servlets (formerly Java Servlets) are the foundation of web applications in Java:

- Servlets are Java classes that handle HTTP requests and responses
- Spring Boot uses an embedded servlet container (default: Tomcat)
- Servlet components are automatically configured through Spring Boot's auto-configuration

### Integration of Annotations and Servlets

Spring Boot simplifies servlet development by:

- Using annotations to configure servlet components
- Providing automatic servlet registration
- Handling the servlet lifecycle through the Spring container
- Supporting both annotation-based and traditional servlet-based development
