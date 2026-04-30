package tungnn.tutor.java.architecture.pattern.t1_mvc.p2_mvp;

// StudentPresenter.java
public class StudentPresenter {

  private final Student model;
  private final StudentView view;

  public StudentPresenter(Student model, StudentView view) {
    this.model = model;
    this.view = view;
  }

  // Logic nghiệp vụ: Cập nhật tên sinh viên
  public void updateStudentName(String name) {
    model.setName(name);
    // Sau khi Model thay đổi, Presenter chủ động bảo View hiển thị lại
    render();
  }

  public void render() {
    view.displayStudentDetails(model.getName(), model.getId());
  }
}
