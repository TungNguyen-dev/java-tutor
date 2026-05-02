package tungnn.tutor.java.pattern.design_pattern.p9_decorator;

abstract class CondimentDecorator extends Beverage {

  protected Beverage beverage; // Đối tượng được bao bọc (wrapped)

  public abstract String getDescription();
}
