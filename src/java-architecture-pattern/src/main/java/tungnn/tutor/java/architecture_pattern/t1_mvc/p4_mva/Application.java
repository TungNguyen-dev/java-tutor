package tungnn.tutor.java.architecture_pattern.t1_mvc.p4_mva;

public class Application {

  static void main() {
    // Khởi tạo các thành phần riêng lẻ
    Student model = new Student("Nguyễn Văn A", 7.5);

    AdminStudentView adminV = new AdminStudentView();
    ParentStudentView parentV = new ParentStudentView();
    JsonExportView jsonV = new JsonExportView();

    // Adapter kết nối tất cả lại
    StudentAdapter adapter = new StudentAdapter(model, adminV, parentV, jsonV);

    // Đồng bộ dữ liệu lần đầu
    adapter.syncAll();

    // Thay đổi dữ liệu
    adapter.updateScore(9.0);
  }
}
