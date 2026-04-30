package tungnn.tutor.java.architecture.pattern.t1_mvc.p2_mvp;

public class StudentViewConsole implements StudentView {

  @Override
  public void displayStudentDetails(String name, String id) {
    System.out.println("--- GIAO DIỆN MVP (CONSOLE) ---");
    System.out.println("ID: " + id);
    System.out.println("Họ tên: " + name);
  }
}
