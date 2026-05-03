package tungnn.tutor.java.tool.v2.application.usecase;

import tungnn.tutor.java.tool.v2.domain.task.Task;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class RunTaskMultiInputUseCase {

  private final RunTaskUseCase singleUseCase;

  public RunTaskMultiInputUseCase(RunTaskUseCase singleUseCase) {
    this.singleUseCase = singleUseCase;
  }

  public <I, O> void execute(Task<I, O> task, List<I> inputList) {
    try (ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor()) {
      List<CompletableFuture<O>> futures =
          inputList.stream()
              .map(
                  input ->
                      CompletableFuture.supplyAsync(
                          () -> singleUseCase.execute(task, input), executor))
              .toList();

      CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();
    }
  }
}
