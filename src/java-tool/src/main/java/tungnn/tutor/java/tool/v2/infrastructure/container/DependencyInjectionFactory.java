package tungnn.tutor.java.tool.v2.infrastructure.container;

import picocli.CommandLine;
import picocli.CommandLine.IFactory;
import tungnn.tutor.java.tool.v2.application.cli.CrawlCommand;
import tungnn.tutor.java.tool.v2.application.usecase.RunTaskMultiInputUseCase;
import tungnn.tutor.java.tool.v2.application.usecase.RunTaskUseCase;
import tungnn.tutor.java.tool.v2.infrastructure.crawler.CourseraSeleniumCrawlTask;
import tungnn.tutor.java.tool.v2.infrastructure.crawler.YoutubeSeleniumCrawlTask;
import tungnn.tutor.java.tool.v2.infrastructure.webdriver.WebDriverPool;

public class DependencyInjectionFactory implements IFactory {
  private final SimpleContainer container;

  public DependencyInjectionFactory() {
    this.container = new SimpleContainer();
    setupDefinitions();
  }

  private void setupDefinitions() {
    // 1. Hạ tầng dùng chung (Singleton)
    int poolSize = Integer.parseInt(System.getenv().getOrDefault("WEB_DRIVER_POOL_SIZE", "1"));
    container.registerLazy(WebDriverPool.class, () -> new WebDriverPool(poolSize));

    // 2. Use Cases (Lazy - chỉ tạo khi có command nào cần dùng)
    container.registerLazy(RunTaskUseCase.class, RunTaskUseCase::new);
    container.registerLazy(
        RunTaskMultiInputUseCase.class,
        () -> new RunTaskMultiInputUseCase(container.get(RunTaskUseCase.class)));

    // 3. Command (Lazy - chỉ tạo khi người dùng gõ lệnh đó)
    container.registerLazy(
        CrawlCommand.class,
        () ->
            new CrawlCommand(
                container.get(RunTaskMultiInputUseCase.class),
                new CourseraSeleniumCrawlTask(container.get(WebDriverPool.class)),
                new YoutubeSeleniumCrawlTask(container.get(WebDriverPool.class))));
  }

  @Override
  public <K> K create(Class<K> cls) throws Exception {
    try {
      return container.get(cls);
    } catch (Exception e) {
      // Nếu không có trong container, thử khởi tạo mặc định (cho Application class)
      return CommandLine.defaultFactory().create(cls);
    }
  }

  public void shutdown() {
    container.closeAll();
  }
}
