package tungnn.tutor.java.pattern.design_pattern.p3_builder;

public class WoodHouseBuilder implements HouseBuilder {
  private House house = new House();

  @Override
  public void buildWalls() {
    house.setWalls("Wood Walls");
  }

  @Override
  public void buildDoors() {
    house.setDoors("Wood Door");
  }

  @Override
  public void buildRoof() {
    house.setRoof("Wood Roof");
  }

  @Override
  public void buildPool() {
    house.setHasSwimmingPool(false);
  }

  @Override
  public House getResult() {
    return this.house;
  }
}
