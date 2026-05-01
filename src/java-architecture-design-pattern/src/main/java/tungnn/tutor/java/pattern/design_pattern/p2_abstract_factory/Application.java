package tungnn.tutor.java.pattern.design_pattern.p2_abstract_factory;

import tungnn.tutor.java.pattern.design_pattern.p2_abstract_factory.creator.FurnitureFactory;
import tungnn.tutor.java.pattern.design_pattern.p2_abstract_factory.creator.ModernFurnitureFactory;
import tungnn.tutor.java.pattern.design_pattern.p2_abstract_factory.creator.VictorianFurnitureFactory;

public class Application {

  static void main(String[] args) {
    FurnitureFactory factory;

    // Giả sử cấu hình hệ thống yêu cầu phong cách Modern
    String config = args[0];

    if (config.equals("MODERN")) {
      factory = new ModernFurnitureFactory();
    } else {
      factory = new VictorianFurnitureFactory();
    }

    // Khởi tạo Client với nhà máy đã chọn
    FurnitureShop shop = new FurnitureShop(factory);
    shop.displayFurniture();
  }
}
