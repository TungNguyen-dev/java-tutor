package tungnn.tutor.java.pattern.design_pattern.p3_builder;

public interface HouseBuilder {
  void buildWalls();

  void buildDoors();

  void buildRoof();

  void buildPool();

  House getResult();
}
