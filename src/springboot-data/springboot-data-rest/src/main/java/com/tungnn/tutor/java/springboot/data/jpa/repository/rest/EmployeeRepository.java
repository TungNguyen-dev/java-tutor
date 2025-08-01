package com.tungnn.tutor.java.springboot.data.jpa.repository.rest;

import com.tungnn.tutor.java.springboot.data.jpa.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(collectionResourceRel = "employees", path = "employees")
public interface EmployeeRepository extends JpaRepository<Employee, Long> {}
