package tungnn.tutor.java.pattern.design_pattern.p7_bridge;

// Implementation Interface
interface Device {
  boolean isEnabled();

  void enable();

  void disable();

  int getVolume();

  void setVolume(int percent);
}

// Concrete Implementation 1: TV
class Tv implements Device {
  private boolean on = false;
  private int volume = 30;

  @Override
  public boolean isEnabled() {
    return on;
  }

  @Override
  public void enable() {
    on = true;
    System.out.println("TV: Đã bật");
  }

  @Override
  public void disable() {
    on = false;
    System.out.println("TV: Đã tắt");
  }

  @Override
  public int getVolume() {
    return volume;
  }

  @Override
  public void setVolume(int percent) {
    this.volume = percent;
    System.out.println("TV: Âm lượng = " + volume);
  }
}

// Concrete Implementation 2: Radio
class Radio implements Device {
  private boolean on = false;
  private int volume = 10;

  @Override
  public boolean isEnabled() {
    return on;
  }

  @Override
  public void enable() {
    on = true;
    System.out.println("Radio: Đã bật");
  }

  @Override
  public void disable() {
    on = false;
    System.out.println("Radio: Đã tắt");
  }

  @Override
  public int getVolume() {
    return volume;
  }

  @Override
  public void setVolume(int percent) {
    this.volume = percent;
    System.out.println("Radio: Âm lượng = " + volume);
  }
}
