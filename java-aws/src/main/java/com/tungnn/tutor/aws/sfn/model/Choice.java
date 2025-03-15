package com.tungnn.tutor.aws.sfn.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import lombok.experimental.SuperBuilder;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(fluent = true)
@SuperBuilder
public class Choice extends State {
    private List<ChoiceRule> choiceRules;

    public boolean hasMultiChoiceRules() {
        return choiceRules != null && choiceRules.size() > 1;
    }
}
