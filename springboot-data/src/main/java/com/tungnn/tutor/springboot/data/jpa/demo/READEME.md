# SPRING & JPA

### Persistence Frameworks
Hibernate
Spring JPA
Database Driver

### Database Configuration and Connection Pooling

### Handling Database Schema and Lazy Loading
ddl-auto
enable_lazy_load_no_trans


## Entity Managers
### JPA Architecture Overview!
[img.png](img.png)

- PersistenceContext
- EntityManager
  - EntityManagerFactory
- Entity
### Entity Management and Transactions
### Querying and Data Manipulation

# CRUD Operations

Basic CRUD (Create, Read, Update, Delete) operations using EntityManager:

- persist() to create/save new entities
- find() to read/get entities by ID
- merge() to update existing entities
- remove() to delete entities

# Queries

JPA Query API using entityManager.createQuery():

- SimpleQuery: Simple queries without parameters
- PositionalParameter: Using positional parameters with ?
- NamedParameter: Using named parameters with :name

# TypedQueries
(Inherited from Queries)

Type-safe version of Query API using entityManager.createQuery() with Class parameter:

- Returns strongly typed results (no need for casting)
- Better compile-time type checking
- Cleaner API

# NamedQueries

Predefined, reusable queries using @NamedQuery annotation:

- Defined at entity level
- Referenced by name
- Validated at startup
- Cached and reusable