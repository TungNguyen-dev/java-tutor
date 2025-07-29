package com.tungnn.tutor.springboot.data.jpa.demo2.repository;

import com.tungnn.tutor.springboot.data.jpa.demo2.entity.Employee;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.jpa.repository.JpaRepository;

@Qualifier("employeeRepository4")
public interface EmployeeRepository4 extends JpaRepository<Employee, Long> {}
