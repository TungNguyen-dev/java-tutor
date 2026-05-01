package tungnn.tutor.java.pattern.design_pattern.p5_singleton;

public class Application {

  public static void main(String[] args) {
    System.out.println("=== KHỞI ĐỘNG HỆ THỐNG ===\n");

    // Sử dụng Eager Singleton
    SystemInfo info = SystemInfo.getInstance();
    System.out.println("Phiên bản hiện tại: " + info.getVersion());

    // Sử dụng Thread-Safe Singleton để ghi lại sự kiện
    Logger logger = Logger.getInstance();
    logger.log("Ứng dụng bắt đầu chạy dịch vụ.");

    // Giả lập một tác vụ cần truy vấn Database
    processDatabaseTask();

    logger.log("Mọi tác vụ hoàn tất. Đang đóng ứng dụng...");
  }

  private static void processDatabaseTask() {
    // Sử dụng Double-Checked Singleton
    DatabaseConnection db = DatabaseConnection.getInstance();
    db.executeQuery("SELECT * FROM users");

    // Thử lấy lại instance lần nữa để chứng minh tính duy nhất
    Logger.getInstance().log("Đã truy xuất dữ liệu người dùng.");
  }
}
