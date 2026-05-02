package tungnn.tutor.java.pattern.design_pattern.p8_composite;

public abstract class MenuComponent {
  // Các phương thức quản lý con (cho Composite)
  public void add(MenuComponent menuComponent) {
    throw new UnsupportedOperationException();
  }

  public void remove(MenuComponent menuComponent) {
    throw new UnsupportedOperationException();
  }

  public MenuComponent getChild(int i) {
    throw new UnsupportedOperationException();
  }

  // Các phương thức hoạt động (cho cả Leaf và Composite)
  public String getName() {
    throw new UnsupportedOperationException();
  }

  public double getPrice() {
    throw new UnsupportedOperationException();
  }

  public abstract void print();
}
