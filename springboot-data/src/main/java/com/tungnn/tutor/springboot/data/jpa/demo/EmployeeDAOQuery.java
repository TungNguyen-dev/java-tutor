package com.tungnn.tutor.springboot.data.jpa.demo;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;

import java.util.List;

/** Using Queries */
public class EmployeeDAOQuery {

  @PersistenceContext private EntityManager entityManager;

  /**
   * Simple query
   */
  public List<?> getAllEmployees() {
    Query query = entityManager.createQuery("select e from Employee e");
    return query.getResultList();
  }

  /**
   * Positional Parameters
   */
  public List<?> getEmployeesInManager(int managerId) {
    Query query = entityManager.createQuery("select e from Employee e where e.managerId = ?");
    query.setParameter(0, managerId);
    return query.getResultList();
  }

  /**
   * Named Parameters
   */
  public List<?> getEmployeesByFirstName(String firstName) {
    Query query = entityManager.createQuery("select e from Employee e where e.firstName = :firstName");
    query.setParameter("firstName", firstName);
    return query.getResultList();
  }
}
