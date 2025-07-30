package com.tungnn.tutor.java.springboot.data.jpa.dao.query;

import com.tungnn.tutor.java.springboot.data.jpa.entity.Employee;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import java.util.List;

/** Using TypedQueries */
public class EmployeeDAOTypedQuery {

  @PersistenceContext private EntityManager entityManager;

  public List<Employee> getAllEmployees() {
    TypedQuery<Employee> query =
        entityManager.createQuery("select e from Employee e", Employee.class);
    return query.getResultList();
  }
}
