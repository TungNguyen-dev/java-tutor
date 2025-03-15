package com.tungnn.tutor.aws.sfn.model;

import lombok.Data;
import lombok.experimental.Accessors;
import lombok.experimental.SuperBuilder;

@Data
@Accessors(fluent = true)
@SuperBuilder
public class State {
    private String stateId;
    private State prev;
    private State next;

    public boolean hasNext() {
        return next != null;
    }

    public String nextStateId() {
        return hasNext() ? next.stateId() : null;
    }
}
