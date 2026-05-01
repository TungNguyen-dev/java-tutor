package tungnn.tutor.java.pattern.design_pattern.p1_factory_method.creator;

import tungnn.tutor.java.pattern.design_pattern.p1_factory_method.product.Pizza;

public abstract class PizzaStore {

  // Factory Method
  protected abstract Pizza createPizza(String type);

  // Template method (business logic cố định)
  public Pizza orderPizza(String type) {
    Pizza pizza = createPizza(type);

    if (pizza == null) {
      throw new IllegalArgumentException("Unknown pizza type");
    }

    pizza.prepare();
    pizza.bake();
    pizza.cut();
    pizza.box();

    return pizza;
  }
}
