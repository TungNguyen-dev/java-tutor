package tungnn.tutor.java.tool.crawler.manager;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import tungnn.tutor.java.tool.crawler.core.CrawlRequest;
import tungnn.tutor.java.tool.crawler.core.CrawlResult;
import tungnn.tutor.java.tool.crawler.core.PageCrawler;
import tungnn.tutor.java.tool.crawler.core.PageCrawlerType;
import tungnn.tutor.java.tool.crawler.pool.PageCrawlerPool;

public class PageCrawlerManager {

  private final PageCrawlerPool pageCrawlerPool;

  public PageCrawlerManager(PageCrawlerPool pageCrawlerPool) {
    this.pageCrawlerPool = pageCrawlerPool;
  }

  /**
   * Accepts a list of crawling requests, processes them in parallel using virtual threads and
   * PageCrawlerPool, and collects the results into a Map.
   */
  public Map<CrawlRequest, CrawlResult> submitBatch(List<CrawlRequest.Page> batchPageRequests) {
    if (batchPageRequests == null || batchPageRequests.isEmpty()) {
      return Map.of();
    }

    Map<CrawlRequest, CrawlResult> resultMap = new ConcurrentHashMap<>();

    // Use Virtual Threads ExecutorService for parallel execution
    try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {
      var futures =
          batchPageRequests.stream()
              .map(
                  request ->
                      CompletableFuture.runAsync(
                          () -> {
                            var result = executeCrawl(request);
                            resultMap.put(request, result);
                          },
                          executor))
              .toList();

      // Wait for all tasks to complete
      CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();
    }

    return resultMap;
  }

  /**
   * Borrows an appropriate crawler from the Pool, performs crawling, and returns it to the Pool.
   */
  private CrawlResult executeCrawl(CrawlRequest.Page request) {
    var crawlerType = PageCrawlerType.resolveType(request);
    PageCrawler crawler = null;
    try {
      crawler = pageCrawlerPool.borrowPageCrawler(crawlerType);
      return crawler.crawl(request);
    } catch (Exception e) {
      return new CrawlResult.Failure(e);
    } finally {
      if (crawler != null) {
        pageCrawlerPool.returnPageCrawler(crawlerType, crawler);
      }
    }
  }
}
