# Application Context by Annotation

## Overview

Spring Application Context is the core container in Spring Framework that manages the lifecycle of
Spring beans and provides advanced enterprise features. This guide focuses on configuring the
Application Context using annotations.

## Configuration with Annotations

Spring's annotation-based configuration provides an alternative to XML-based configuration. It
enables more concise bean definitions and reduces configuration overhead.

## Key Annotations

- `@Configuration`: Indicates that a class declares one or more `@Bean` methods
- `@ComponentScan`: Enables component scanning for Spring beans
- `@Component`: Marks a class as a Spring component
- `@Service`: Indicates that a class is a service layer component
- `@Repository`: Marks a class as a data access component
- `@Controller`: Indicates that a class is a web controller
- `@Autowired`: Enables dependency injection
- `@Bean`: Declares a method that produces a bean

## Component Scanning

Component scanning allows Spring to automatically detect and register beans from specified base
packages:

