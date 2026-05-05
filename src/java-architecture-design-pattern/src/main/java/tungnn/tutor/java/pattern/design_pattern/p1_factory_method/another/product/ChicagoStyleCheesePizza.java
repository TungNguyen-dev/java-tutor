package tungnn.tutor.java.pattern.design_pattern.p1_factory_method.another.product;

public class ChicagoStyleCheesePizza extends Pizza {
  public ChicagoStyleCheesePizza() {
    name = "Chicago Style Cheese Pizza (Deep Dish)";
  }

  @Override
  public void cut() {
    System.out.println("Cutting into square slices");
  }
}
