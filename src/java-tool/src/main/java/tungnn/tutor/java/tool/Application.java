package tungnn.tutor.java.tool;

import io.github.classgraph.ClassGraph;
import io.github.classgraph.ScanResult;
import picocli.CommandLine;
import picocli.CommandLine.Command;

import java.util.List;

@Command(name = "", mixinStandardHelpOptions = true, version = "1.0")
public class Application implements Runnable {

  static void main(String[] args) {
    CommandLine cmd = new CommandLine(new Application());

    // Tự động quét và đăng ký subcommands
    try (ScanResult scanResult =
        new ClassGraph()
            .enableAnnotationInfo()
            .acceptPackages("tungnn.tutor.java.tool.application.command")
            .scan()) {

      // Tìm các class có đánh dấu @Command
      List<Class<?>> commandClasses =
          scanResult.getClassesWithAnnotation(Command.class.getName()).loadClasses();

      for (Class<?> cls : commandClasses) {
        // Tránh việc add chính class Application vào làm subcommand của nó
        if (!cls.equals(Application.class)) {
          cmd.addSubcommand(cls);
        }
      }
    } catch (Exception e) {
      System.err.println(e.getMessage());
      cmd.usage(System.err);
    }

    System.exit(cmd.execute(args));
  }

  @Override
  public void run() {
    new CommandLine(this).usage(System.out);
  }
}
