package tungnn.tutor.java.pattern.design_pattern.p2_abstract_factory;

import tungnn.tutor.java.pattern.design_pattern.p2_abstract_factory.creator.FurnitureFactory;
import tungnn.tutor.java.pattern.design_pattern.p2_abstract_factory.product.Chair;
import tungnn.tutor.java.pattern.design_pattern.p2_abstract_factory.product.Sofa;

public class FurnitureShop {

  private Chair chair;
  private Sofa sofa;

  public FurnitureShop(FurnitureFactory factory) {
    // Client chỉ biết về interface, không biết về lớp cụ thể (Modern hay Victorian)
    chair = factory.createChair();
    sofa = factory.createSofa();
  }

  public void displayFurniture() {
    chair.sitOn();
    sofa.lieOn();
  }
}
