package com.tungnn.tutor.java.springboot.data.jpa.service;

import com.tungnn.tutor.java.springboot.data.jpa.entity.Employee;
import com.tungnn.tutor.java.springboot.data.jpa.repository.EmployeeJpaRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(rollbackFor = Exception.class)
public class EmployeeServiceImpl implements EmployeeService {

  private final EmployeeJpaRepository employeeRepository;

  @Override
  public Employee createEmployee(Employee employee) {
    return employeeRepository.save(employee);
  }

  @Override
  public Employee getEmployeeById(Long id) {
    return employeeRepository.findById(id).orElse(null);
  }

  @Override
  public List<Employee> getAllEmployees() {
    return employeeRepository.findAll();
  }

  @Override
  public Employee updateEmployee(Employee employee) {
    return employeeRepository.save(employee);
  }

  @Override
  public void deleteEmployee(Long id) {
    employeeRepository.deleteById(id);
  }
}
