package tungnn.tutor.java.pattern.design_pattern.p1_factory_method.another.creator;

import tungnn.tutor.java.pattern.design_pattern.p1_factory_method.another.product.NYStyleCheesePizza;
import tungnn.tutor.java.pattern.design_pattern.p1_factory_method.another.product.Pizza;

public class NYPizzaStore extends PizzaStore {

  @Override
  protected Pizza createPizza(String type) {
    if ("cheese".equals(type)) {
      return new NYStyleCheesePizza();
    }
    return null;
  }
}
