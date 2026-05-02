package tungnn.tutor.java.pattern.design_pattern.p9_decorator;

public class Application {

  static void main(String[] args) {
    // 1. Order một ly Espresso đơn thuần
    Beverage beverage = new Espresso();
    System.out.println(beverage.getDescription() + " $" + beverage.cost());

    // 2. Order một ly Dark Roast, thêm Mocha, thêm Mocha (double), rồi thêm Whip
    Beverage beverage2 = new DarkRoast();
    beverage2 = new Mocha(beverage2); // Bọc DarkRoast trong Mocha
    beverage2 = new Mocha(beverage2); // Bọc Mocha(DarkRoast) trong Mocha thứ hai
    beverage2 = new Whip(beverage2); // Bọc tất cả trong Whip

    System.out.println(beverage2.getDescription() + " $" + String.format("%.2f", beverage2.cost()));
    // Kết quả kỳ vọng: 0.99 (DarkRoast) + 0.20 (Mocha) + 0.20 (Mocha) + 0.10 (Whip) = 1.49
  }
}
