package tungnn.tutor.java.pattern.design_pattern.p2_abstract_factory.creator;

import tungnn.tutor.java.pattern.design_pattern.p2_abstract_factory.product.Chair;
import tungnn.tutor.java.pattern.design_pattern.p2_abstract_factory.product.Sofa;

public interface FurnitureFactory {

  Chair createChair();

  Sofa createSofa();
}
