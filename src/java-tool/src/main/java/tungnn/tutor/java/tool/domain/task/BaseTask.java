package tungnn.tutor.java.tool.domain.task;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.Callable;

public abstract class BaseTask<T> implements Callable<T> {

  protected final Logger logger = LoggerFactory.getLogger(this.getClass());
  protected final String taskId;

  protected BaseTask(String taskId) {
    this.taskId = taskId;
  }

  @Override
  public T call() throws Exception {
    Instant start = Instant.now();
    logger.info("Bắt đầu thực thi Task [{}]: {}", taskId, this.getClass().getSimpleName());

    try {
      // Thực thi logic chính của task được định nghĩa ở lớp con
      T result = execute();

      long elapsed = Duration.between(start, Instant.now()).toMillis();
      logger.info("Hoàn thành Task [{}] trong {}ms", taskId, elapsed);
      return result;

    } catch (Exception e) {
      logger.error("Lỗi xảy ra tại Task [{}]: {}", taskId, e.getMessage());
      // Bạn có thể thực hiện logic cleanup hoặc retry ở đây
      throw e;
    } finally {
      cleanup();
    }
  }

  protected abstract T execute() throws Exception;

  protected void cleanup() {
    // Mặc định không làm gì, lớp con có thể override
  }
}
