package com.tungnn.tutor.java.springboot.data.jpa.service;

import com.tungnn.tutor.java.springboot.data.jpa.entity.Employee;
import java.util.List;

public interface EmployeeService {
  Employee createEmployee(Employee employee);

  Employee getEmployeeById(Long id);

  List<Employee> getAllEmployees();

  Employee updateEmployee(Employee employee);

  void deleteEmployee(Long id);
}
