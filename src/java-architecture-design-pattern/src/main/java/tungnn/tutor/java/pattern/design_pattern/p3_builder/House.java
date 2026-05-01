package tungnn.tutor.java.pattern.design_pattern.p3_builder;

public class House {
  private String walls;
  private String doors;
  private String roof;
  private boolean hasSwimmingPool;

  // Các setter được Builder sử dụng
  public void setWalls(String walls) {
    this.walls = walls;
  }

  public void setDoors(String doors) {
    this.doors = doors;
  }

  public void setRoof(String roof) {
    this.roof = roof;
  }

  public void setHasSwimmingPool(boolean hasSwimmingPool) {
    this.hasSwimmingPool = hasSwimmingPool;
  }

  @Override
  public String toString() {
    return "House [Walls="
        + walls
        + ", Doors="
        + doors
        + ", Roof="
        + roof
        + ", Pool="
        + (hasSwimmingPool ? "Yes" : "No")
        + "]";
  }
}
