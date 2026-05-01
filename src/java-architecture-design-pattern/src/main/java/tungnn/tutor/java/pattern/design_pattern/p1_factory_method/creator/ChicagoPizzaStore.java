package tungnn.tutor.java.pattern.design_pattern.p1_factory_method.creator;

import tungnn.tutor.java.pattern.design_pattern.p1_factory_method.product.ChicagoStyleCheesePizza;
import tungnn.tutor.java.pattern.design_pattern.p1_factory_method.product.Pizza;

public class ChicagoPizzaStore extends PizzaStore {

  @Override
  protected Pizza createPizza(String type) {
    if ("cheese".equals(type)) {
      return new ChicagoStyleCheesePizza();
    }
    return null;
  }
}
