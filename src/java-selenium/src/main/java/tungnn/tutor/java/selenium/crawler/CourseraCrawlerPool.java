package tungnn.tutor.java.selenium.crawler;

import org.openqa.selenium.WebDriver;
import tungnn.tutor.java.selenium.util.SeleniumUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.*;

public class CourseraCrawlerPool implements AutoCloseable {

  private final BlockingQueue<CourseraCrawler> pool;

  public CourseraCrawlerPool(int poolSize) {
    this.pool = new ArrayBlockingQueue<>(poolSize);

    try (ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor()) {
      List<Future<CourseraCrawler>> futures = new ArrayList<>();
      for (int i = 0; i < poolSize; i++) {
        Callable<CourseraCrawler> task =
            () -> {
              WebDriver driver = SeleniumUtil.createChromeDriver();
              return new CourseraCrawler(driver);
            };
        futures.add(executor.submit(task));
      }
      for (Future<CourseraCrawler> future : futures) {
        pool.add(future.get());
      }
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
      throw new RuntimeException("Crawler pool initialization interrupted", e);
    } catch (ExecutionException e) {
      throw new RuntimeException("Failed to initialize crawler", e.getCause());
    }
  }

  public CourseraCrawler borrowCrawler() throws InterruptedException {
    return pool.take(); // blocks if none available
  }

  public void returnCrawler(CourseraCrawler crawler) {
    if (Objects.isNull(crawler)) {
      return;
    }
    boolean rs = pool.offer(crawler);
    if (!rs) {
      throw new RuntimeException("Could not add Coursera crawler to the pool");
    }
  }

  @Override
  public void close() {
    for (CourseraCrawler crawler : pool) {
      crawler.close();
    }
  }
}
