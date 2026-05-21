void main(String[] args) {
  System.out.println("=== KHỞI ĐỘNG HỆ THỐNG ===\n");

  // Sử dụng Eager Singleton
  EagerSingletonSystemInfo info = EagerSingletonSystemInfo.getInstance();
  System.out.println("Phiên bản hiện tại: " + info.getVersion());

  // Sử dụng Thread-Safe Singleton để ghi lại sự kiện
  ThreadSafeSingletonLogger threadSafeSingletonLogger = ThreadSafeSingletonLogger.getInstance();
  threadSafeSingletonLogger.log("Ứng dụng bắt đầu chạy dịch vụ.");

  // Giả lập một tác vụ cần truy vấn Database
  processDatabaseTask();

  threadSafeSingletonLogger.log("Mọi tác vụ hoàn tất. Đang đóng ứng dụng...");
}

private static void processDatabaseTask() {
  // Sử dụng Double-Checked Singleton
  DoubleCheckedSingletonDbConnection db = DoubleCheckedSingletonDbConnection.getInstance();
  db.executeQuery("SELECT * FROM users");

  // Thử lấy lại instance lần nữa để chứng minh tính duy nhất
  ThreadSafeSingletonLogger.getInstance().log("Đã truy xuất dữ liệu người dùng.");
}

// 1. Eager: Thông tin hệ thống (Khởi tạo ngay)
public static class EagerSingletonSystemInfo {
  private static final EagerSingletonSystemInfo instance = new EagerSingletonSystemInfo();

  private EagerSingletonSystemInfo() {}

  public static EagerSingletonSystemInfo getInstance() {
    return instance;
  }

  public String getVersion() {
    return "v2.0.26";
  }
}

// 2. Double-Checked: Kết nối Database (Khởi tạo khi cần, tối ưu hiệu năng)
public static class DoubleCheckedSingletonDbConnection {
  private static volatile DoubleCheckedSingletonDbConnection instance;

  private DoubleCheckedSingletonDbConnection() {
    System.out.println("-> Đã kết nối Database thành công.");
  }

  public static DoubleCheckedSingletonDbConnection getInstance() {
    if (instance == null) {
      synchronized (DoubleCheckedSingletonDbConnection.class) {
        if (instance == null) instance = new DoubleCheckedSingletonDbConnection();
      }
    }
    return instance;
  }

  public void executeQuery(String sql) {
    System.out.println("-> Thực thi: " + sql);
  }
}

// 3. Thread-Safe: Bộ ghi Log (Đơn giản, đảm bảo an toàn đa luồng)
public static class ThreadSafeSingletonLogger {
  private static ThreadSafeSingletonLogger instance;

  private ThreadSafeSingletonLogger() {}

  public static synchronized ThreadSafeSingletonLogger getInstance() {
    if (instance == null) instance = new ThreadSafeSingletonLogger();
    return instance;
  }

  public void log(String message) {
    System.out.println("[LOG " + System.currentTimeMillis() + "]: " + message);
  }
}
