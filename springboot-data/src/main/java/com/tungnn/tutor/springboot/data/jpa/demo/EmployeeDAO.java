package com.tungnn.tutor.springboot.data.jpa.demo;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

/**
 * Simple CRUD operations
 */
public class EmployeeDAO {

  @PersistenceContext private EntityManager entityManager;

  public Employee getEmployeeById(int id) {
    return entityManager.find(Employee.class, id);
  }

  public void saveEmployee(Employee employee) {
    entityManager.persist(employee);
  }

  public void updateEmployee(Employee employee) {
    entityManager.merge(employee);
  }

  public void deleteEmployee(Employee employee) {
    entityManager.remove(employee);
  }
}
