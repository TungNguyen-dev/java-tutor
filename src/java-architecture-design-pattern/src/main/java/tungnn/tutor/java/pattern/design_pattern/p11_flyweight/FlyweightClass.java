package tungnn.tutor.java.pattern.design_pattern.p11_flyweight;

// Flyweight: Chứa trạng thái nội tại (Intrinsic State)
class TreeType {
  private String name;
  private String color;
  private String otherTreeData; // Giả sử đây là dữ liệu texture rất nặng

  public TreeType(String name, String color, String otherTreeData) {
    this.name = name;
    this.color = color;
    this.otherTreeData = otherTreeData;
  }

  public void draw(int x, int y) {
    System.out.println("Vẽ cây " + name + " [Màu: " + color + "] tại tọa độ (" + x + "," + y + ")");
  }
}
