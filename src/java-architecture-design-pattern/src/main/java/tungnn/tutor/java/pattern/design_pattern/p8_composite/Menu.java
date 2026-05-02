package tungnn.tutor.java.pattern.design_pattern.p8_composite;

import java.util.ArrayList;
import java.util.List;

public class Menu extends MenuComponent {

  private final List<MenuComponent> menuComponents = new ArrayList<>();
  private final String name;

  public Menu(String name) {
    this.name = name;
  }

  @Override
  public void add(MenuComponent menuComponent) {
    menuComponents.add(menuComponent);
  }

  @Override
  public void remove(MenuComponent menuComponent) {
    menuComponents.remove(menuComponent);
  }

  @Override
  public MenuComponent getChild(int i) {
    return menuComponents.get(i);
  }

  @Override
  public String getName() {
    return name;
  }

  @Override
  public void print() {
    System.out.println("\nMENU: " + getName());
    System.out.println("---------------------");

    // Sử dụng đệ quy để in tất cả các thành phần con
    for (MenuComponent component : menuComponents) {
      component.print();
    }
  }
}
