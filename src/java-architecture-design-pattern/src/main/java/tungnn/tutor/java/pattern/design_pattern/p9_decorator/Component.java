package tungnn.tutor.java.pattern.design_pattern.p9_decorator;

abstract class Beverage {
  String description = "Unknown Beverage";

  public String getDescription() {
    return description;
  }

  public abstract double cost();
}
