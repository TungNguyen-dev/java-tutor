package tungnn.tutor.java.pattern.design_pattern.p1_factory_method.creator;

import tungnn.tutor.java.pattern.design_pattern.p1_factory_method.product.NYStyleCheesePizza;
import tungnn.tutor.java.pattern.design_pattern.p1_factory_method.product.Pizza;

public class NYPizzaStore extends PizzaStore {

  @Override
  protected Pizza createPizza(String type) {
    if ("cheese".equals(type)) {
      return new NYStyleCheesePizza();
    }
    return null;
  }
}
