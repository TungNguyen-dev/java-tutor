package com.tungnn.tutor.springboot.data.jpa.demo;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import java.util.List;

/** Using TypedQueries */
public class EmployeeDAOQuery2 {

  @PersistenceContext private EntityManager entityManager;

  public List<Employee> getAllEmployees() {
    TypedQuery<Employee> query =
        entityManager.createQuery("select e from Employee e", Employee.class);
    return query.getResultList();
  }
}
