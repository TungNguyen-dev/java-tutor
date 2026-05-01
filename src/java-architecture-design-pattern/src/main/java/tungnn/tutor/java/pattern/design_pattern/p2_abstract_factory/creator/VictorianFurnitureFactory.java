package tungnn.tutor.java.pattern.design_pattern.p2_abstract_factory.creator;

import tungnn.tutor.java.pattern.design_pattern.p2_abstract_factory.product.Chair;
import tungnn.tutor.java.pattern.design_pattern.p2_abstract_factory.product.Sofa;
import tungnn.tutor.java.pattern.design_pattern.p2_abstract_factory.product.VictorianChair;
import tungnn.tutor.java.pattern.design_pattern.p2_abstract_factory.product.VictorianSofa;

public class VictorianFurnitureFactory implements FurnitureFactory {

  public Chair createChair() {
    return new VictorianChair();
  }

  public Sofa createSofa() {
    return new VictorianSofa();
  }
}
