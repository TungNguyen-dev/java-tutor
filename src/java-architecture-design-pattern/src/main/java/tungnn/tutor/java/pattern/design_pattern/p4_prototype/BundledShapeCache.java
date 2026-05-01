package tungnn.tutor.java.pattern.design_pattern.p4_prototype;

import java.util.HashMap;
import java.util.Map;

public class BundledShapeCache {
  private Map<String, Shape> cache = new HashMap<>();

  public BundledShapeCache() {
    Circle circle = new Circle();
    circle.x = 10;
    circle.y = 20;
    circle.radius = 15;
    circle.color = "Red";

    cache.put("Big red circle", circle);

    Rectangle rectangle = new Rectangle();
    rectangle.width = 10;
    rectangle.height = 20;
    rectangle.color = "Blue";

    cache.put("Medium blue rectangle", rectangle);
  }

  public Shape get(String key) {
    return cache.get(key).clone(); // Trả về bản sao
  }
}
