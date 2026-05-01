package tungnn.tutor.java.pattern.design_pattern.p3_builder;

public class Application {

  static void main() {
    HouseDirector director = new HouseDirector();

    // Xây nhà gỗ đơn giản
    HouseBuilder woodBuilder = new WoodHouseBuilder();
    director.constructSimpleHouse(woodBuilder);
    House simpleWoodHouse = woodBuilder.getResult();

    System.out.println("Result: " + simpleWoodHouse);
  }
}
