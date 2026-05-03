package tungnn.tutor.java.tool.v2.domain.task;

import java.util.UUID;

public class TaskExecution {

  private final String executionId;
  private TaskStatus taskStatus;

  public TaskExecution() {
    this.executionId = "exec-" + UUID.randomUUID();
    this.taskStatus = TaskStatus.PENDING;
  }

  public String getExecutionId() {
    return executionId;
  }

  public TaskStatus getTaskStatus() {
    return taskStatus;
  }

  public void markRunning() {
    this.taskStatus = TaskStatus.RUNNING;
  }

  public void markCompleted() {
    this.taskStatus = TaskStatus.COMPLETED;
  }

  public void markFailed() {
    this.taskStatus = TaskStatus.FAILED;
  }

  public void markCancelled() {
    this.taskStatus = TaskStatus.CANCELLED;
  }
}
