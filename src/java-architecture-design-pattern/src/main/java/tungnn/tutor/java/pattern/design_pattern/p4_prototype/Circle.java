package tungnn.tutor.java.pattern.design_pattern.p4_prototype;

public class Circle extends Shape {
  public int radius;

  public Circle() {}

  // Copy Constructor của lớp con
  public Circle(Circle target) {
    super(target); // Gọi constructor cha để copy x, y, color
    if (target != null) {
      this.radius = target.radius;
    }
  }

  @Override
  public Shape clone() {
    return new Circle(this);
  }
}
