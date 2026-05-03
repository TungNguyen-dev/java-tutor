package tungnn.tutor.java.tool.v2;

import picocli.CommandLine;
import picocli.CommandLine.Command;
import tungnn.tutor.java.tool.v2.application.cli.CrawlCommand;
import tungnn.tutor.java.tool.v2.infrastructure.container.DependencyInjectionFactory;

@Command(
    name = "tutor-tool",
    mixinStandardHelpOptions = true,
    version = "2.0",
    description = "Bộ công cụ tự động hóa đào tạo",
    subcommands = {CrawlCommand.class})
public class Application implements Runnable {

  static void main(String[] args) {
    int exitCode;
    DependencyInjectionFactory factory = null;

    try {
      // 1. Khởi tạo factory
      factory = new DependencyInjectionFactory();

      // Đăng ký Shutdown Hook để đảm bảo đóng tài nguyên khi System.exit() được gọi
      // hoặc khi người dùng nhấn Ctrl+C
      DependencyInjectionFactory finalFactory = factory;
      Runtime.getRuntime().addShutdownHook(new Thread(finalFactory::shutdown));

      CommandLine cmd = new CommandLine(new Application(), factory);

      // 2. Xử lý lỗi thực thi nội bộ của Command
      cmd.setExecutionExceptionHandler(
          (ex, commandLine, parseResult) -> {
            System.err.printf(">>> Lỗi thực thi: %s%n", ex.getMessage());
            return 1;
          });

      // 3. Chạy command và lấy exit code
      exitCode = cmd.execute(args);

    } catch (Exception e) {
      System.err.println(">>> Không thể khởi động ứng dụng!");
      System.err.println(">>> Chi tiết lỗi khởi tạo: " + e.getMessage());
      exitCode = 1;
    }

    // 4. Thoát chương trình
    // Lúc này Shutdown Hook sẽ được kích hoạt để gọi factory.shutdown()
    System.exit(exitCode);
  }

  @Override
  public void run() {
    CommandLine.usage(this, System.out);
  }
}
