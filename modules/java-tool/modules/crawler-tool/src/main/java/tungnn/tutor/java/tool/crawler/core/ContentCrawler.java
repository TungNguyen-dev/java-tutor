package tungnn.tutor.java.tool.crawler.core;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public interface ContentCrawler {

  Executor VIRTUAL_THREAD_EXECUTOR = Executors.newVirtualThreadPerTaskExecutor();

  ContentCrawlResult crawl(ContentCrawlRequest request);

  default ContentCrawlResult crawlSafe(ContentCrawlRequest request) {
    try {
      return crawl(request);
    } catch (Exception e) {
      return ContentCrawlResult.failure(
          request != null ? request.url() : "UNKNOWN_URL",
          e.getMessage() != null ? e.getMessage() : e.getClass().getSimpleName());
    }
  }

  default ContentCrawlResult crawl(String url) {
    return crawlSafe(new ContentCrawlRequest(url));
  }

  default CompletableFuture<ContentCrawlResult> crawlAsync(ContentCrawlRequest request) {
    return CompletableFuture.supplyAsync(() -> crawlSafe(request), VIRTUAL_THREAD_EXECUTOR)
        .exceptionally(
            throwable ->
                ContentCrawlResult.failure(
                    request != null ? request.url() : "UNKNOWN_URL",
                    throwable.getCause() != null
                        ? throwable.getCause().getMessage()
                        : throwable.getMessage()));
  }

  default List<ContentCrawlResult> crawlBatch(List<ContentCrawlRequest> requests) {
    List<CompletableFuture<ContentCrawlResult>> futures =
        requests.stream().map(this::crawlAsync).toList();

    return futures.stream().map(CompletableFuture::join).toList();
  }
}
