package tungnn.tutor.java.pattern.design_pattern.p0_simple_factory;

class Product {}

class ProductCreator {
  // Simple factory method
  public Product createProduct() {
    return new Product();
  }
}

public class SimpleFactory {

  static void main() {
    ProductCreator productCreator = new ProductCreator();
    Product product = productCreator.createProduct();
    System.out.println(product.getClass().getSimpleName());
  }
}
