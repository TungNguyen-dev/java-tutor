package tungnn.tutor.java.pattern.design_pattern.p11_flyweight;

// Context: Chứa trạng thái ngoại lai (Extrinsic State)
class Tree {
  private int x;
  private int y;
  private TreeType type; // Tham chiếu đến Flyweight

  public Tree(int x, int y, TreeType type) {
    this.x = x;
    this.y = y;
    this.type = type;
  }

  public void draw() {
    type.draw(x, y);
  }
}
