package com.tungnn.tutor.aws.sfn.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import lombok.experimental.SuperBuilder;

@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(fluent = true)
@SuperBuilder
public class Task extends State {
}
