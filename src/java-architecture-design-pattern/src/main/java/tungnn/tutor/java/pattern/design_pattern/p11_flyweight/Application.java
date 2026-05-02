package tungnn.tutor.java.pattern.design_pattern.p11_flyweight;

import java.util.ArrayList;
import java.util.List;

public class Application {

  private List<Tree> trees = new ArrayList<>();

  public void plantTree(int x, int y, String name, String color, String otherTreeData) {
    TreeType type = TreeFactory.getTreeType(name, color, otherTreeData);
    Tree tree = new Tree(x, y, type);
    trees.add(tree);
  }

  public void draw() {
    for (Tree tree : trees) {
      tree.draw();
    }
  }

  public static void main(String[] args) {
    Application forest = new Application();

    // Trồng hàng ngàn cây nhưng thực tế chỉ có 2 loại đối tượng TreeType được tạo ra
    forest.plantTree(1, 2, "Sồi", "Xanh lá", "Texture_Sồi_4K");
    forest.plantTree(5, 3, "Sồi", "Xanh lá", "Texture_Sồi_4K");
    forest.plantTree(10, 10, "Thông", "Xanh thẫm", "Texture_Thông_4K");
    forest.plantTree(20, 30, "Sồi", "Xanh lá", "Texture_Sồi_4K");

    forest.draw();

    System.out.println("--------------------------------------");
    System.out.println("Tổng số cây đã trồng: 4");
    System.out.println("Dữ liệu nặng (TreeType) thực tế trong bộ nhớ: 2");
  }
}
