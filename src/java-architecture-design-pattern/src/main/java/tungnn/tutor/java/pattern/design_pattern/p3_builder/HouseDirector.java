package tungnn.tutor.java.pattern.design_pattern.p3_builder;

public class HouseDirector {
  public void constructLuxuryHouse(HouseBuilder builder) {
    builder.buildWalls();
    builder.buildDoors();
    builder.buildRoof();
    builder.buildPool(); // Nhà sang trọng thì có hồ bơi
  }

  public void constructSimpleHouse(HouseBuilder builder) {
    builder.buildWalls();
    builder.buildDoors();
    builder.buildRoof();
    // Không gọi buildPool
  }
}
