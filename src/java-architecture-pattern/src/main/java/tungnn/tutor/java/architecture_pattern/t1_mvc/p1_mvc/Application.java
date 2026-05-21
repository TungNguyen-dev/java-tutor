package tungnn.tutor.java.architecture_pattern.t1_mvc.p1_mvc;

public class Application {

  static void main() {
    // 1. Tạo Model
    Student model = new Student("S123", "Nguyễn Văn A");

    // 2. Tạo View và đăng ký làm Observer của Model
    StudentView view = new StudentView();
    model.addObserver(view);

    // 3. Tạo Controller
    StudentController controller = new StudentController(model);

    // 4. Giả lập tương tác người dùng
    System.out.println("User yêu cầu đổi tên thành 'Trần Văn B'...");
    controller.updateStudentName("Trần Văn B");

    System.out.println("\nUser yêu cầu đổi tên thành 'Lê Thị C'...");
    controller.updateStudentName("Lê Thị C");
  }
}
