package com.tungnn.tutor.springboot.data.jpa.demo3.entity;

import jakarta.persistence.Embeddable;
import java.io.Serializable;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Embeddable
public class JobHistoryId implements Serializable {
  private static final long serialVersionUID = -1816453550087330326L;
}
