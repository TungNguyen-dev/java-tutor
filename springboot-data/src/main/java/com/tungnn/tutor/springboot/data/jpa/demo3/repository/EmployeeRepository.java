package com.tungnn.tutor.springboot.data.jpa.demo3.repository;

import com.tungnn.tutor.springboot.data.jpa.demo3.entity.Employee;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(collectionResourceRel = "employees", path = "employees")
public interface EmployeeRepository extends JpaRepository<Employee, Long> {

  List<Employee> findAllByLastName(String lastName);
}
