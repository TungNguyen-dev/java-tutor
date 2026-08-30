package tungnn.tutor.java.tool.crawler.core;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadLocalRandom;
import tungnn.tutor.java.core.lib.multithread.ThreadUtil;

public class BatchCrawlExecutor {

  // Default Backoff configurations
  private static final long INITIAL_INTERVAL_MS = 500;
  private static final double BACKOFF_MULTIPLIER = 2.0;
  private static final long MAX_INTERVAL_MS = 5000;

  // Thời gian thực thi tối thiểu cho mỗi request thành công
  private static final long MIN_EXECUTION_TIME_MS = 60_000;

  private final ContentCrawler crawler;
  private final Executor executor;

  public BatchCrawlExecutor(ContentCrawler crawler, Executor executor) {
    this.crawler = crawler;
    this.executor = executor != null ? executor : Executors.newVirtualThreadPerTaskExecutor();
  }

  public BatchCrawlExecutor(ContentCrawler crawler) {
    this(crawler, Executors.newVirtualThreadPerTaskExecutor());
  }

  public CompletableFuture<ContentCrawlResult> crawlAsync(ContentCrawlRequest request) {
    return CompletableFuture.supplyAsync(() -> executeWithRetry(request), executor);
  }

  public List<ContentCrawlResult> crawlBatch(List<ContentCrawlRequest> requests) {
    List<CompletableFuture<ContentCrawlResult>> futures =
        requests.stream().map(this::crawlAsync).toList();

    return futures.stream().map(CompletableFuture::join).toList();
  }

  private ContentCrawlResult executeWithRetry(ContentCrawlRequest request) {
    int maxRetries = 3;
    int attempt = 0;
    long currentInterval = INITIAL_INTERVAL_MS;
    Exception lastException;

    while (true) {
      long startTime = System.currentTimeMillis();

      try {
        attempt++;
        // 1. Thực thi crawl
        ContentCrawlResult result = crawler.crawl(request);

        // 2. Tính toán thời gian đã trôi qua
        long elapsedTime = System.currentTimeMillis() - startTime;

        // 3. Nếu crawl thành công nhưng chưa đủ 15s, sleep phần thời gian còn thiếu
        if (elapsedTime < MIN_EXECUTION_TIME_MS) {
          ThreadUtil.sleep(MIN_EXECUTION_TIME_MS - elapsedTime);
        }

        return result;

      } catch (Exception e) {
        lastException = e;

        if (attempt >= maxRetries) {
          break;
        }

        long sleepTime = calculateBackoffWithJitter(currentInterval);
        ThreadUtil.sleep(sleepTime);

        currentInterval = (long) Math.min(currentInterval * BACKOFF_MULTIPLIER, MAX_INTERVAL_MS);
      }
    }

    return ContentCrawlResult.failure(
        request != null ? request.url() : "UNKNOWN_URL",
        "Failed after " + maxRetries + " attempts. Root cause: " + lastException.getMessage());
  }

  private long calculateBackoffWithJitter(long interval) {
    return ThreadLocalRandom.current().nextLong(interval / 2, interval + 1);
  }
}
