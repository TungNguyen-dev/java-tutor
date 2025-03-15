package com.tungnn.tutor.aws.sfn;

import com.tungnn.tutor.aws.sfn.model.Choice;
import com.tungnn.tutor.aws.sfn.model.ChoiceRule;
import com.tungnn.tutor.aws.sfn.model.Parallel;
import com.tungnn.tutor.aws.sfn.model.State;
import com.tungnn.tutor.aws.sfn.model.StateMachine;
import com.tungnn.tutor.aws.sfn.model.Task;
import com.tungnn.tutor.aws.sfn.model.Wait;
import com.tungnn.tutor.aws.util.Utils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;

public class StateMachineGenerator {

    public static final String STATE_MACHINE_TEMPLATE = "";
    public static final String TASK_TEMPLATE = "";
    public static final String PARALLEL_TEMPLATE = "";
    public static final String WAIT_TEMPLATE = "";
    public static final String CHOICE_TEMPLATE = "";
    public static final String CHOICE_RULE_TEMPLATE = "";
    public static final String CHOICE_RULE_WITHOUT_NEXT_TEMPLATE = "";

    public void generateStateMachine(StateMachine stateMachine, Path outputDir) throws IOException {
        String renderedStateMachine = renderStateMachine(stateMachine);
        Files.writeString(outputDir, renderedStateMachine);
    }

    private String renderStateMachine(StateMachine stateMachine) {
        StringBuilder sb = new StringBuilder(STATE_MACHINE_TEMPLATE);

        StringBuilder statesBuilder = new StringBuilder();
        for (int i = 0; i < stateMachine.states().size(); i++) {
            State state = stateMachine.states().get(i);
            statesBuilder.append("\"").append(state.stateId()).append("\": ")
                    .append(renderState(state))
                    .append(",");
        }
        String endingStates = """
                              "SuccessState": {
                                "Type": "Succeed"
                              },
                              "FailureState": {
                                "Type": "Fail"
                                "Error": "ExecutionFailed",
                                "Cause": "Condition was not met"
                              }
                              """;
        statesBuilder.append(endingStates);

        Utils.replacePlaceholder(sb, "{{STATE_MACHINE_ID}}", stateMachine.stateMachineId());
        Utils.replacePlaceholder(sb, "{{START_AT}}", stateMachine.states().getFirst().stateId());
        Utils.replacePlaceholder(sb, "{{STATES_LIST}}", statesBuilder.toString());

        return sb.toString();
    }

    private String renderState(State state) {
        return switch (state) {
            case Task task -> renderTask(task);
            case Parallel parallel -> renderParallel(parallel);
            case Wait wait -> renderWait(wait);
            case Choice choice -> renderChoice(choice);
            default -> throw new IllegalStateException("Unexpected value: " + state);
        };
    }

    private String renderTask(Task task) {
        StringBuilder sb = new StringBuilder(TASK_TEMPLATE);
        Utils.replacePlaceholder(sb, "{{ECS_CLUSTER_NAME}}", "");
        Utils.replacePlaceholder(sb, "{{TASK_DEFINITION_ARN}}", "");
        Utils.replacePlaceholder(sb, "{{SUBNET_NAME_ARRAY}}", "");
        Utils.replacePlaceholder(sb, "{{SECURITY_GROUP_NAME_ARRAY}}", "");
        Utils.replacePlaceholder(sb, "{{CONTAINER_OVERRIDE_OBJECT_ARRAY}}", "");
        Utils.replacePlaceholder(sb, "{{NEXT_ATTRIBUTE}}", Utils.parseNextAttribute(task));

        return sb.toString();
    }

    private String renderParallel(Parallel parallel) {
        StringBuilder sb = new StringBuilder(PARALLEL_TEMPLATE);

        String[] branches = new String[parallel.branches().size()];
        for (int i = 0; i < parallel.branches().size(); i++) {
            branches[i] = renderState(parallel.branches().get(i));
        }

        Utils.replacePlaceholder(sb, "{{BRANCHES_ARRAY}}", Arrays.toString(branches));
        Utils.replacePlaceholder(sb, "{{NEXT_ATTRIBUTE}}", Utils.parseNextAttribute(parallel));

        return sb.toString();
    }

    private String renderWait(Wait wait) {
        StringBuilder sb = new StringBuilder(WAIT_TEMPLATE);
        Utils.replacePlaceholder(sb, "{{WAIT_SECONDS}}", String.valueOf(wait.seconds()));
        Utils.replacePlaceholder(sb, "{{NEXT_STATE_ID}}", wait.nextStateId());

        return sb.toString();
    }

    private String renderChoice(Choice choice) {
        StringBuilder sb = new StringBuilder(CHOICE_TEMPLATE);

        String[] choices = new String[choice.choiceRules().size()];
        for (int i = 0; i < choice.choiceRules().size(); i++) {
            choices[i] = renderChoiceRule(choice.choiceRules().get(i));
        }

        String choicesArray = Arrays.toString(choices);
        if (choice.hasMultiChoiceRules()) {
            String andTemplate = """
                                 {
                                   "And": {{CHOICES_ARRAY}},
                                   "Next": {{NEXT_STATE_ID}}
                                 }""";
            choicesArray = andTemplate
                    .replace("{{CHOICES_ARRAY}}", choicesArray)
                    .replace("{{NEXT_STATE_ID}}", choice.nextStateId());
        }

        Utils.replacePlaceholder(sb, "{{CHOICES_ARRAY}}", choicesArray);

        return sb.toString();
    }

    private String renderChoiceRule(ChoiceRule choiceRule) {
        StringBuilder sb;

        if (choiceRule.hasNext()) {
            sb = new StringBuilder(CHOICE_RULE_TEMPLATE);
        } else {
            sb = new StringBuilder(CHOICE_RULE_WITHOUT_NEXT_TEMPLATE);
        }

        Utils.replacePlaceholder(sb, "{{VARIABLE}}", choiceRule.variable());
        Utils.replacePlaceholder(sb, "{{COMPARISON}}", choiceRule.comparison());
        Utils.replacePlaceholder(sb, "{{NEXT_ATTRIBUTE}}", choiceRule.nextStateId());

        return sb.toString();
    }
}
