package tungnn.tutor.java.pattern.architecture_pattern.t4_onion.infrastructure;

import tungnn.tutor.java.pattern.architecture_pattern.t4_onion.domain.Order;
import tungnn.tutor.java.pattern.architecture_pattern.t4_onion.domain.OrderRepository;

public class InMemoryOrderRepository implements OrderRepository {

  @Override
  public void save(Order order) {
    System.out.println("[DB] Lưu đơn hàng " + order.getId() + " thành công!");
  }
}
