package tungnn.tutor.java.pattern.architecture_pattern.t1_mvc.p1_mvc;

// StudentView.java
public class StudentView implements StudentObserver {

  @Override
  public void update(String name, String id) {
    System.out.println("\n--- VIEW TỰ CẬP NHẬT (Observer) ---");
    System.out.println("Dữ liệu mới nhận từ Model:");
    System.out.println("ID: " + id);
    System.out.println("Tên: " + name);
  }
}