package tungnn.tutor.java.tool.v2.application.usecase;

import tungnn.tutor.java.tool.v2.domain.task.Task;
import tungnn.tutor.java.tool.v2.domain.task.TaskExecution;
import tungnn.tutor.java.tool.v2.domain.task.TaskInstance;

public class RunTaskUseCase {

  private static final int MAX_RETRIES = 3;

  public <I, O> O execute(Task<I, O> task, I input) {
    TaskInstance instance = new TaskInstance(task.getName());
    System.out.printf("Starting task %s - TaskId %s%n", task.getName(), instance.getInstanceId());
    return executeWithRetry(task, input, instance);
  }

  protected <I, O> O executeWithRetry(Task<I, O> task, I input, TaskInstance instance) {
    int attempt = 0;
    Exception lastException = null;

    while (attempt < MAX_RETRIES) {
      TaskExecution execution = new TaskExecution();
      instance.addExecution(execution);
      attempt++;

      try {
        System.out.printf(
            "[INFO] Executing %s - Attempt %d/%d - Input %s%n",
            execution.getExecutionId(), attempt, MAX_RETRIES, input);

        execution.markRunning();
        O result = task.execute(input);
        execution.markCompleted();

        // Xử lý chuỗi output để log không bị vỡ dòng
        String logResult = result != null ? result.toString().replace("\n", "\\n") : "null";
        System.out.printf(
            "[INFO] Completed %s - Attempt %d/%d - Output %s%n",
            task.getName(), attempt, MAX_RETRIES, logResult);

        return result;
      } catch (Exception e) {
        execution.markFailed();
        lastException = e;

        System.err.printf(
            "[ERROR] Task %s failed on attempt %d: %s%n", task.getName(), attempt, e.getMessage());

        if (attempt < MAX_RETRIES) {
          backoff(attempt);
        }
      }
    }

    System.err.println("[FATAL] Task " + task.getName() + " exhausted all retries.");
    throw new RuntimeException("Task failed after " + MAX_RETRIES + " attempts", lastException);
  }

  private void backoff(int attempt) {
    long sleepTime = (long) Math.pow(2, attempt) * 1000;
    System.out.println("[RETRY] Waiting " + (sleepTime / 1000) + "s before next attempt...");
    try {
      Thread.sleep(sleepTime);
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
    }
  }
}
