package tungnn.tutor.java.csv;

import org.apache.commons.csv.CSVFormat;

import java.nio.file.Path;
import java.util.List;

public class CsvDemo {

  public record User(String id, String name, String email, int age) {}

  static void main(String[] args) {
    demoCsvWithRecord();
  }

  private static void demoCsvWithRecord() {
    Path userPath = Path.of("storage", "users.csv");

    // 1. Create sample data
    List<User> users =
        List.of(
            new User("1", "Tung", "tung@gmail.com", 25),
            new User("2", "An", "an@gmail.com", 30),
            new User("3", "Binh", "binh@gmail.com", 20));

    // 2. Write CSV
    CsvUtil.write(userPath, CSVFormat.DEFAULT, users, User.class);

    // 3. Read CSV
    var readUsers =
        CsvUtil.read(
            userPath,
            CSVFormat.DEFAULT.builder().setHeader().setSkipHeaderRecord(true).build(),
            User.class);

    // 4. Print result
    System.out.println("=== Users ===");
    readUsers.forEach(System.out::println);
  }
}
