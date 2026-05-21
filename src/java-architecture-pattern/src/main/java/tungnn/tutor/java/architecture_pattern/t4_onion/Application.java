package tungnn.tutor.java.architecture_pattern.t4_onion;

import tungnn.tutor.java.architecture_pattern.t4_onion.application.OrderService;
import tungnn.tutor.java.architecture_pattern.t4_onion.container.AppContainer;

public class Application {

  static void main() {
    // Khởi động container
    var container = new AppContainer();

    // Lấy OrderService từ container
    var orderService = container.getService(OrderService.class);

    // Thực thi nghiệp vụ
    System.out.println("--- Bắt đầu tạo đơn hàng ---");
    orderService.createOrder("ORD-123", 500.0);
  }
}
