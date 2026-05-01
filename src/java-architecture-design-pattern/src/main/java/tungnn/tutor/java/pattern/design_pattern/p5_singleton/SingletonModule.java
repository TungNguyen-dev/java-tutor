package tungnn.tutor.java.pattern.design_pattern.p5_singleton;

// 1. Eager: Thông tin hệ thống (Khởi tạo ngay)
class SystemInfo {
  private static final SystemInfo instance = new SystemInfo();

  private SystemInfo() {}

  public static SystemInfo getInstance() {
    return instance;
  }

  public String getVersion() {
    return "v2.0.26";
  }
}

// 2. Double-Checked: Kết nối Database (Khởi tạo khi cần, tối ưu hiệu năng)
class DatabaseConnection {
  private static volatile DatabaseConnection instance;

  private DatabaseConnection() {
    System.out.println("-> Đã kết nối Database thành công.");
  }

  public static DatabaseConnection getInstance() {
    if (instance == null) {
      synchronized (DatabaseConnection.class) {
        if (instance == null) instance = new DatabaseConnection();
      }
    }
    return instance;
  }

  public void executeQuery(String sql) {
    System.out.println("-> Thực thi: " + sql);
  }
}

// 3. Thread-Safe: Bộ ghi Log (Đơn giản, đảm bảo an toàn đa luồng)
class Logger {
  private static Logger instance;

  private Logger() {}

  public static synchronized Logger getInstance() {
    if (instance == null) instance = new Logger();
    return instance;
  }

  public void log(String message) {
    System.out.println("[LOG " + System.currentTimeMillis() + "]: " + message);
  }
}
