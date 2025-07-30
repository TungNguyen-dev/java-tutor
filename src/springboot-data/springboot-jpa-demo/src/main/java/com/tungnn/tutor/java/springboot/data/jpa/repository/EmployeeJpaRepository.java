package com.tungnn.tutor.java.springboot.data.jpa.repository;

import com.tungnn.tutor.java.springboot.data.jpa.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmployeeJpaRepository extends JpaRepository<Employee, Long> {}
