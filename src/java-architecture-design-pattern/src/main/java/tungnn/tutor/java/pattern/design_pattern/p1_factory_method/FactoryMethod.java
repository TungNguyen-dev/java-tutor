package tungnn.tutor.java.pattern.design_pattern.p1_factory_method;

abstract class Product {}

class FirstProduct extends Product {}

class SecondProduct extends Product {}

// ---

interface ProductCreator {

  // Abstract FactoryMethod
  Product createProduct();
}

class FirstProductCreator implements ProductCreator {
  @Override
  public Product createProduct() {
    return new FirstProduct();
  }
}

class SecondProductCreator implements ProductCreator {
  @Override
  public Product createProduct() {
    return new SecondProduct();
  }
}

public class FactoryMethod {

  static void main() {
    ProductCreator product1Creator = new FirstProductCreator();
    Product product1 = product1Creator.createProduct();
    System.out.println(product1.getClass().getSimpleName());

    ProductCreator product2Creator = new SecondProductCreator();
    Product product2 = product2Creator.createProduct();
    System.out.println(product2.getClass().getSimpleName());
  }
}
