package com.tungnn.tutor.springboot.data.jpa.demo2.entity;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;
import java.time.LocalDate;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Getter
@Setter
@Entity
@Table(name = "JOB_HISTORY", schema = "HR")
public class JobHistory {
  @EmbeddedId private JobHistoryId id;

  @MapsId
  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @OnDelete(action = OnDeleteAction.RESTRICT)
  @JoinColumn(name = "EMPLOYEE_ID", nullable = false)
  private Employee employee;

  @Column(name = "END_DATE", nullable = false)
  private LocalDate endDate;
}
