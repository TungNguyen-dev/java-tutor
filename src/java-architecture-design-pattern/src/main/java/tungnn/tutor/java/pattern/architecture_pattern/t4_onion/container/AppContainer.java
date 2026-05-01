package tungnn.tutor.java.pattern.architecture_pattern.t4_onion.container;

// container/AppContainer.java

import tungnn.tutor.java.pattern.architecture_pattern.t4_onion.application.OrderService;
import tungnn.tutor.java.pattern.architecture_pattern.t4_onion.domain.OrderRepository;
import tungnn.tutor.java.pattern.architecture_pattern.t4_onion.infrastructure.InMemoryOrderRepository;

import java.util.HashMap;
import java.util.Map;

public class AppContainer {
  private final Map<Class<?>, Object> services = new HashMap<>();

  public AppContainer() {
    bootstrap();
  }

  private void bootstrap() {
    // 1. Khởi tạo Infrastructure (Lớp ngoài cùng)
    OrderRepository orderRepo = new InMemoryOrderRepository();

    // 2. "Tiêm" Infrastructure vào Application (Layer bên trong)
    OrderService orderService = new OrderService(orderRepo);

    // Đưa vào map để quản lý
    services.put(OrderRepository.class, orderRepo);
    services.put(OrderService.class, orderService);
  }

  @SuppressWarnings("unchecked")
  public <T> T getService(Class<T> serviceClass) {
    return (T) services.get(serviceClass);
  }
}
