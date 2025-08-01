package com.tungnn.tutor.java.springboot.data.jpa.repository;

import com.tungnn.tutor.java.springboot.data.jpa.entity.Employee;
import org.springframework.data.repository.Repository;

public interface EmployeeRepository extends Repository<Employee, Long> {}
