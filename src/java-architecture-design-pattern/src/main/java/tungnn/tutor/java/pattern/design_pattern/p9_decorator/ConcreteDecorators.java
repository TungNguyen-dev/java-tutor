package tungnn.tutor.java.pattern.design_pattern.p9_decorator;

class Mocha extends CondimentDecorator {
  public Mocha(Beverage beverage) {
    this.beverage = beverage;
  }

  public String getDescription() {
    return beverage.getDescription() + ", Mocha";
  }

  public double cost() {
    return beverage.cost() + 0.20; // Giá cafe gốc + 0.20
  }
}

class Whip extends CondimentDecorator {
  public Whip(Beverage beverage) {
    this.beverage = beverage;
  }

  public String getDescription() {
    return beverage.getDescription() + ", Whip";
  }

  public double cost() {
    return beverage.cost() + 0.10; // Giá cafe trước đó + 0.10
  }
}
