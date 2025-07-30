package com.tungnn.tutor.java.springboot.data.jpa.dao.query;

import com.tungnn.tutor.java.springboot.data.jpa.entity.Employee;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;

/** Using NamedQueries */
public class EmployeeDAONamedQuery {

  @PersistenceContext private EntityManager entityManager;

  public Employee getEmployeeByLastName(String lastName) {
    Query query = entityManager.createNamedQuery("Employee.findByLastName");
    query.setParameter("lastName", lastName);
    return (Employee) query.getSingleResult();
  }
}
