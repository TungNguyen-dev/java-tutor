package tungnn.tutor.java.pattern.design_pattern.p1_factory_method;

import tungnn.tutor.java.pattern.design_pattern.p1_factory_method.creator.ChicagoPizzaStore;
import tungnn.tutor.java.pattern.design_pattern.p1_factory_method.creator.NYPizzaStore;
import tungnn.tutor.java.pattern.design_pattern.p1_factory_method.creator.PizzaStore;
import tungnn.tutor.java.pattern.design_pattern.p1_factory_method.product.Pizza;

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
