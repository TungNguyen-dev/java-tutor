package tungnn.tutor.java.pattern.design_pattern.p1_factory_method.another.product;

public class NYStyleCheesePizza extends Pizza {
  public NYStyleCheesePizza() {
    name = "NY Style Cheese Pizza (Thin Crust)";
  }

  @Override
  public void cut() {
    System.out.println("Cutting into diagonal slices");
  }
}
