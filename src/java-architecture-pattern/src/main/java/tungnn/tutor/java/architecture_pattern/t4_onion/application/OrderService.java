package tungnn.tutor.java.architecture_pattern.t4_onion.application;

import tungnn.tutor.java.architecture_pattern.t4_onion.domain.Order;
import tungnn.tutor.java.architecture_pattern.t4_onion.domain.OrderRepository;

public class OrderService {

  private final OrderRepository repository;

  public OrderService(OrderRepository repository) {
    this.repository = repository;
  }

  public void createOrder(String id, double amount) {
    Order order = new Order(id, amount);
    order.validate();
    repository.save(order);
  }
}
