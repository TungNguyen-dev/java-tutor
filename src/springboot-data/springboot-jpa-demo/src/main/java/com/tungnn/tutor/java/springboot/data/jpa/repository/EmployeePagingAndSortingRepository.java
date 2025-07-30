package com.tungnn.tutor.java.springboot.data.jpa.repository;

import com.tungnn.tutor.java.springboot.data.jpa.entity.Employee;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface EmployeePagingAndSortingRepository
    extends PagingAndSortingRepository<Employee, Long> {}
