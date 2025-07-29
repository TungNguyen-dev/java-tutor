package com.tungnn.tutor.springboot.data.jpa.demo3.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Getter
@Setter
@Entity
@Table(name = "DEPARTMENTS", schema = "HR")
public class Department {
  @Id
  @Column(name = "DEPARTMENT_ID", nullable = false)
  private Short id;

  @Column(name = "DEPARTMENT_NAME", nullable = false, length = 30)
  private String departmentName;

  @ManyToOne(fetch = FetchType.LAZY)
  @OnDelete(action = OnDeleteAction.RESTRICT)
  @JoinColumn(name = "MANAGER_ID")
  private Employee manager;
}
