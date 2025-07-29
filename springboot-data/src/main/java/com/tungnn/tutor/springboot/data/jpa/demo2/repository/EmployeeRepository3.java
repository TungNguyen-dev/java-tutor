package com.tungnn.tutor.springboot.data.jpa.demo2.repository;

import com.tungnn.tutor.springboot.data.jpa.demo2.entity.Employee;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.repository.PagingAndSortingRepository;

@Qualifier("employeeRepository3")
public interface EmployeeRepository3 extends PagingAndSortingRepository<Employee, Long> {}
