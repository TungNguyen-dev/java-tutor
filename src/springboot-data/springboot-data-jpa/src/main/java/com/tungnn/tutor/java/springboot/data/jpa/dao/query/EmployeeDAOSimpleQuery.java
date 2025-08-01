package com.tungnn.tutor.java.springboot.data.jpa.dao.query;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import java.util.List;

/** Using Queries */
public class EmployeeDAOSimpleQuery {

  @PersistenceContext private EntityManager entityManager;

  /** Simple query */
  public List<?> getAllEmployees() {
    Query query = entityManager.createQuery("select e from Employee e");
    return query.getResultList();
  }

  /** Positional Parameters */
  public List<?> getEmployeesByFirstName(String firstName) {
    Query query = entityManager.createQuery("select e from Employee e where e.firstName = ?");
    query.setParameter(0, firstName);
    return query.getResultList();
  }

  /** Named Parameters */
  public List<?> getEmployeesByFirstName2(String firstName) {
    Query query =
        entityManager.createQuery("select e from Employee e where e.firstName = :firstName");
    query.setParameter("firstName", firstName);
    return query.getResultList();
  }
}
