package com.tungnn.tutor.springboot.data.jpa.demo2.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDate;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "EMPLOYEES", schema = "HR")
@NamedQuery(
    name = "Employee.findByLastName",
    query = "select e from Employee e where e.lastName = :lastName")
public class Employee {
  @Id
  @Column(name = "EMPLOYEE_ID", nullable = false)
  private Integer id;

  @Column(name = "FIRST_NAME", length = 20)
  private String firstName;

  @Column(name = "LAST_NAME", nullable = false, length = 25)
  private String lastName;

  @Column(name = "EMAIL", nullable = false, length = 25)
  private String email;

  @Column(name = "PHONE_NUMBER", length = 20)
  private String phoneNumber;

  @Column(name = "HIRE_DATE", nullable = false)
  private LocalDate hireDate;

  @Column(name = "SALARY", precision = 8, scale = 2)
  private BigDecimal salary;

  @Column(name = "COMMISSION_PCT", precision = 2, scale = 2)
  private BigDecimal commissionPct;
}
