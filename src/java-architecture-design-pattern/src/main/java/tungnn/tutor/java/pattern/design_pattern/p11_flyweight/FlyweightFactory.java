package tungnn.tutor.java.pattern.design_pattern.p11_flyweight;

import java.util.HashMap;
import java.util.Map;

// Flyweight Factory: Quản lý việc lưu trữ và tái sử dụng
class TreeFactory {
  private static Map<String, TreeType> treeTypes = new HashMap<>();

  public static TreeType getTreeType(String name, String color, String otherTreeData) {
    String key = name + "_" + color;
    TreeType result = treeTypes.get(key);

    if (result == null) {
      result = new TreeType(name, color, otherTreeData);
      treeTypes.put(key, result);
      System.out.println("== Tạo mới loại cây: " + name + " ==");
    }
    return result;
  }
}
