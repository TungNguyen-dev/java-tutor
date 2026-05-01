package tungnn.tutor.java.pattern.design_pattern.p2_abstract_factory.creator;

import tungnn.tutor.java.pattern.design_pattern.p2_abstract_factory.product.Chair;
import tungnn.tutor.java.pattern.design_pattern.p2_abstract_factory.product.ModernChair;
import tungnn.tutor.java.pattern.design_pattern.p2_abstract_factory.product.ModernSofa;
import tungnn.tutor.java.pattern.design_pattern.p2_abstract_factory.product.Sofa;

public class ModernFurnitureFactory implements FurnitureFactory {

  public Chair createChair() {
    return new ModernChair();
  }

  public Sofa createSofa() {
    return new ModernSofa();
  }
}
