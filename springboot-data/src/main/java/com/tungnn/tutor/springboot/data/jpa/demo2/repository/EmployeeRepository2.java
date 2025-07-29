package com.tungnn.tutor.springboot.data.jpa.demo2.repository;

import com.tungnn.tutor.springboot.data.jpa.demo2.entity.Employee;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.repository.CrudRepository;

@Qualifier("employeeRepository2")
public interface EmployeeRepository2 extends CrudRepository<Employee, Long> {}
