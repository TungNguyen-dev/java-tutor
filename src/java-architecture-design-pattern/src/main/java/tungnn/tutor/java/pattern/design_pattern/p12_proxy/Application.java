package tungnn.tutor.java.pattern.design_pattern.p12_proxy;

public class Application {

  static void main() {
    Document myDoc = new DocumentProxy("BaoCaoTaiChinh.pdf", "Doanh thu: 1 tỷ VNĐ");

    // 1. Nhân viên bình thường xem tài liệu
    System.out.println("--- Lượt truy cập 1 ---");
    myDoc.displayContent("STAFF");
    myDoc.editContent("STAFF", "Sửa doanh thu thành 0 VNĐ"); // Sẽ bị chặn

    System.out.println("\n--- Lượt truy cập 2 ---");
    // 2. Quản trị viên truy cập
    myDoc.displayContent("ADMIN");
    myDoc.editContent("ADMIN", "Doanh thu: 1.5 tỷ VNĐ"); // Thành công
  }
}
