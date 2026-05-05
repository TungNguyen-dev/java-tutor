package tungnn.tutor.java.pattern.design_pattern.p1_factory_method.another.product;

public abstract class Pizza {
  String name;

  public void prepare() {
    System.out.println("Preparing " + name);
  }

  public void bake() {
    System.out.println("Baking " + name);
  }

  public void cut() {
    System.out.println("Cutting " + name);
  }

  public void box() {
    System.out.println("Boxing " + name);
  }

  public String getName() {
    return name;
  }
}
