package com.tungnn.tutor.springboot.data.jpa.demo2.repository;

import com.tungnn.tutor.springboot.data.jpa.demo2.entity.Employee;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.repository.Repository;

@Qualifier("employeeRepository")
public interface EmployeeRepository extends Repository<Employee, Long> {}
