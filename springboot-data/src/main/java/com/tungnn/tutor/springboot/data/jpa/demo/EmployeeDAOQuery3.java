package com.tungnn.tutor.springboot.data.jpa.demo;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;

/** Using NamedQueries */
public class EmployeeDAOQuery3 {

  @PersistenceContext private EntityManager entityManager;

  public Employee getEmployeeByLastName(String lastName) {
    Query query = entityManager.createNamedQuery("Employee.findByLastName");
    query.setParameter("lastName", lastName);
    return (Employee) query.getSingleResult();
  }
}
