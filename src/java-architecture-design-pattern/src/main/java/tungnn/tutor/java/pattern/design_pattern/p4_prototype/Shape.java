package tungnn.tutor.java.pattern.design_pattern.p4_prototype;

// Base Prototype
public abstract class Shape {
  public int x;
  public int y;
  public String color;

  // Constructor thông thường
  public Shape() {}

  // Copy Constructor: Chìa khóa để sao chép các field private/protected
  public Shape(Shape target) {
    if (target != null) {
      this.x = target.x;
      this.y = target.y;
      this.color = target.color;
    }
  }

  // Phương thức clone trừu tượng
  public abstract Shape clone();

  @Override
  public boolean equals(Object object2) {
    if (!(object2 instanceof Shape)) return false;
    Shape shape2 = (Shape) object2;
    return shape2.x == x && shape2.y == y && shape2.color.equals(color);
  }
}
