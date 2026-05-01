package tungnn.tutor.java.pattern.design_pattern.p4_prototype;

public class Application {

  static void main() {
    BundledShapeCache cache = new BundledShapeCache();

    // Lấy mẫu từ Registry
    Shape shape1 = cache.get("Big red circle");
    Shape shape2 = cache.get("Big red circle");

    if (shape1 != shape2 && shape1.equals(shape2)) {
      System.out.println(
          "Hai hình này là các đối tượng khác nhau nhưng có nội dung giống hệt nhau!");
    } else {
      System.out.println("Lỗi: Đối tượng không giống nhau hoặc trùng tham chiếu.");
    }
  }
}
