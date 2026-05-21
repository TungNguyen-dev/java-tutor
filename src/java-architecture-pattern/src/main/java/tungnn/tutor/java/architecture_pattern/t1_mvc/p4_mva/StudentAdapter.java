package tungnn.tutor.java.architecture_pattern.t1_mvc.p4_mva;

public class StudentAdapter {
  private Student model;
  private AdminStudentView adminView;
  private ParentStudentView parentView;
  private JsonExportView jsonView;

  public StudentAdapter(Student model, AdminStudentView av, ParentStudentView pv, JsonExportView jv) {
    this.model = model;
    this.adminView = av;
    this.parentView = pv;
    this.jsonView = jv;
  }

  public void syncAll() {
    // 1. Dữ liệu thô cho Admin
    if (adminView != null) {
      adminView.renderFullDetails(model.getName(), model.getScore());
    }

    // 2. Logic "phiên dịch" xếp loại cho Phụ huynh
    if (parentView != null) {
      String rank = (model.getScore() >= 8.0) ? "XUẤT SẮC" : "CẦN CỐ GẮNG";
      parentView.displayGrade(model.getName(), rank);
    }

    // 3. Logic chuyển đổi định dạng cho Hệ thống
    if (jsonView != null) {
      String json = String.format("{\"student\": \"%s\", \"score\": %.1f}",
              model.getName(), model.getScore());
      jsonView.export(json);
    }
  }

  public void updateScore(double newScore) {
    model.setScore(newScore);
    System.out.println("\n--- Đã cập nhật điểm mới cho Model ---");
    syncAll();
  }
}