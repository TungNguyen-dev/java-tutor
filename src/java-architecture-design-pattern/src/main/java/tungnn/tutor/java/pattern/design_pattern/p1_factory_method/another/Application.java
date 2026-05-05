package tungnn.tutor.java.pattern.design_pattern.p1_factory_method.another;

import tungnn.tutor.java.pattern.design_pattern.p1_factory_method.another.creator.ChicagoPizzaStore;
import tungnn.tutor.java.pattern.design_pattern.p1_factory_method.another.creator.NYPizzaStore;
import tungnn.tutor.java.pattern.design_pattern.p1_factory_method.another.creator.PizzaStore;
import tungnn.tutor.java.pattern.design_pattern.p1_factory_method.another.product.Pizza;

public class Application {

  static void main() {
    PizzaStore nyStore = new NYPizzaStore();
    PizzaStore chicagoStore = new ChicagoPizzaStore();

    Pizza pizza1 = nyStore.orderPizza("cheese");
    System.out.println("Ordered: " + pizza1.getName());

    System.out.println("----------------------");

    Pizza pizza2 = chicagoStore.orderPizza("cheese");
    System.out.println("Ordered: " + pizza2.getName());
  }
}
