## DEMO Java RestTemplate Clients

### getObject vs getForEntity

- `getObject()`: Returns only the response body mapped to specified type
- `getForEntity()`: Returns `ResponseEntity` containing status code, headers and body

### exchange()

- Flexible method allowing customization of:
    - HTTP method (GET, POST, PUT, etc.)
    - Request headers
    - Request body (entity)
    - Response type
- Returns `ResponseEntity`

### ParameterizedTypeReference

- Enables type-safe deserialization of generic types in response body
- Example usage: `new ParameterizedTypeReference<List<User>>()`
- Preserves generic type information at runtime