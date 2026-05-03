package tungnn.tutor.java.tool.v2.domain.task;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class TaskInstance {

  private final String instanceId;
  private final String instanceName;
  private final Set<TaskExecution> executions;

  public TaskInstance(String instanceName) {
    this.instanceId = "inst-" + UUID.randomUUID();
    this.instanceName = instanceName;
    this.executions = new HashSet<>();
  }

  public String getInstanceId() {
    return instanceId;
  }

  public String getInstanceName() {
    return instanceName;
  }

  public Set<TaskExecution> getExecutions() {
    return executions;
  }

  public void addExecution(TaskExecution execution) {
    executions.add(execution);
  }
}
