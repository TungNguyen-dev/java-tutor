package tungnn.tutor.java.tool.crawler;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class ApplicationTests {

  @Test
  @DisplayName("Kiểm tra chạy thử nghiệm JUnit 6")
  void hello() {
    assertDoesNotThrow(
        () -> {
          System.out.println("Hello World!");
        });
  }
}
