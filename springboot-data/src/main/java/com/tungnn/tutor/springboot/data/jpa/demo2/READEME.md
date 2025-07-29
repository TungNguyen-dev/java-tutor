# JPA Repositories

Spring Data JPA provides robust repository support based on the JPA EntityManager.

## Implementation

Spring Data JPA offers flexible implementation that abstracts away the boilerplate code required
for data access layers, allowing developers to focus on business logic.

## Proxy

Spring Data JPA uses proxy-based implementations - developers only need to define repository
interfaces, and Spring provides the implementation automatically at runtime.

## Auto-generating Queries

Spring Data JPA can automatically generate queries from method names and provides flexible ways to
define custom queries using the @Query annotation.