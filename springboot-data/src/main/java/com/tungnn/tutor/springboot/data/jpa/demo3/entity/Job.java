package com.tungnn.tutor.springboot.data.jpa.demo3.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "JOBS", schema = "HR")
public class Job {
  @Id
  @Column(name = "JOB_ID", nullable = false, length = 10)
  private String jobId;

  @Column(name = "JOB_TITLE", nullable = false, length = 35)
  private String jobTitle;

  @Column(name = "MIN_SALARY")
  private Integer minSalary;

  @Column(name = "MAX_SALARY")
  private Integer maxSalary;
}
