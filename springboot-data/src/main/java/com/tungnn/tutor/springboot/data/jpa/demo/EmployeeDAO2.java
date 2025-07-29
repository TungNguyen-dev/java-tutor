package com.tungnn.tutor.springboot.data.jpa.demo;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.PersistenceUnit;

public class EmployeeDAO2 {

  @PersistenceUnit private EntityManagerFactory entityManagerFactory;

  private EntityManager entityManager;

  @PostConstruct
  public void init() {
    entityManager = entityManagerFactory.createEntityManager();
  }

  @PreDestroy
  public void cleanup() {
    entityManager.close();
  }

  public void saveEmployee(Employee employee) {
    EntityTransaction transaction = entityManager.getTransaction();

    try {
      transaction.begin();
      entityManager.persist(employee);
      transaction.commit();
    } catch (Exception e) {
      if (transaction.isActive()) {
        transaction.rollback();
      }
      throw e;
    }
  }
}
