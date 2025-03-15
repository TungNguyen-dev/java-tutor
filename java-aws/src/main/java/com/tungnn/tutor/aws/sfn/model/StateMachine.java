package com.tungnn.tutor.aws.sfn.model;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Accessors(fluent = true)
public class StateMachine {
    private String stateMachineId;
    private List<State> states;
}
