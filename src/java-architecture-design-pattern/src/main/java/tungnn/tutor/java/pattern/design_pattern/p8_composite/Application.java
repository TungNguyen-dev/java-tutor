package tungnn.tutor.java.pattern.design_pattern.p8_composite;

public class Application {

  static void main() {
    // Tạo các Menu cấp cao
    MenuComponent pancakeHouseMenu = new Menu("PANCAKE HOUSE MENU");
    MenuComponent dinerMenu = new Menu("DINER MENU");
    MenuComponent dessertMenu = new Menu("DESSERT MENU");

    // Cấu trúc cây chính
    MenuComponent allMenus = new Menu("ALL MENUS");
    allMenus.add(pancakeHouseMenu);
    allMenus.add(dinerMenu);

    // Thêm các Leaf (món ăn)
    pancakeHouseMenu.add(new MenuItem("K&B's Pancake Breakfast", 2.99));
    dinerMenu.add(new MenuItem("Vegetarian BLT", 2.99));
    dinerMenu.add(new MenuItem("Pasta", 3.89));

    // Thêm Composite vào Composite (Menu trong Menu)
    dinerMenu.add(dessertMenu);
    dessertMenu.add(new MenuItem("Apple Pie", 1.59));

    // Client xử lý đồng nhất toàn bộ cấu trúc
    allMenus.print();
  }
}
