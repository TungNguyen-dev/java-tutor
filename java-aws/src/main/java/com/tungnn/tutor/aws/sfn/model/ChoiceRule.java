package com.tungnn.tutor.aws.sfn.model;

public record ChoiceRule(
        String variable,
        String comparison,
        State next) {

    public boolean hasNext() {
        return next != null;
    }

    public String nextStateId() {
        return hasNext() ? next.stateId() : null;
    }
}
