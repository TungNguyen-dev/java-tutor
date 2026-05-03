package tungnn.tutor.java.tool.v2.infrastructure.container;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class SimpleContainer {
  private final Map<Class<?>, Supplier<?>> definitions = new HashMap<>();
  private final Map<Class<?>, Object> singletons = new HashMap<>();

  /** Đăng ký một service dưới dạng Singleton (khởi tạo ngay) */
  public <T> void register(Class<T> type, T instance) {
    singletons.put(type, instance);
  }

  /** Đăng ký một service dưới dạng Lazy Singleton (chỉ khởi tạo khi được gọi lần đầu) */
  public <T> void registerLazy(Class<T> type, Supplier<T> supplier) {
    definitions.put(type, supplier);
  }

  /** Lấy instance của một class. Nếu là lazy, nó sẽ khởi tạo và lưu vào kho singleton. */
  @SuppressWarnings("unchecked")
  public <T> T get(Class<T> type) {
    if (singletons.containsKey(type)) {
      return (T) singletons.get(type);
    }

    if (definitions.containsKey(type)) {
      T instance = (T) definitions.get(type).get();
      singletons.put(type, instance);
      return instance;
    }

    throw new RuntimeException("Không tìm thấy service đăng ký cho: " + type.getName());
  }

  /** Giải phóng tài nguyên của các instance có hỗ trợ đóng (AutoCloseable) */
  public void closeAll() {
    singletons.values().stream()
        .filter(obj -> obj instanceof AutoCloseable)
        .forEach(
            obj -> {
              try {
                ((AutoCloseable) obj).close(); // Phải đảm bảo logic quit driver nằm ở đây
              } catch (Exception e) {
                // Log lỗi nhưng không được dừng vòng lặp
                System.err.println("Lỗi đóng tài nguyên lẻ: " + e.getMessage());
              }
            });
  }
}
