package tungnn.tutor.java.pattern.architecture_pattern.t1_mvc.p2_mvp;

public class Application {

  static void main() {
    // Khởi tạo các thành phần
    Student model = new Student("S999", "Lê Văn C");
    StudentView view = new StudentViewConsole();

    // Khởi tạo Presenter
    StudentPresenter presenter = new StudentPresenter(model, view);

    // Hiển thị lần đầu
    presenter.render();

    // Cập nhật thông tin
    System.out.println("\nĐang cập nhật tên qua Presenter...");
    presenter.updateStudentName("Phạm Thị D");
  }
}
