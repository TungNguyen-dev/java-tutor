# Spring REST

This project demonstrates how to use Spring Data REST to automatically expose Spring Data JPA
repositories as RESTful services.

## Dependencies

### spring-boot-starter-data-rest

This dependency provides the core functionality to expose Spring Data repositories as REST
resources:

- Automatically creates RESTful web services for Spring Data repositories
- Supports standard HTTP methods (GET, POST, PUT, DELETE, etc.)
- Handles request/response content negotiation
- Provides hypermedia-driven responses
- Supports pagination and sorting

### spring-data-rest-hal-explorer

- Web-based HAL browser interface for exploring REST endpoints
- Allows interactive testing of REST APIs
- Provides documentation and discoverability of available resources
- Visualizes links between resources
- Supports content negotiation and format switching