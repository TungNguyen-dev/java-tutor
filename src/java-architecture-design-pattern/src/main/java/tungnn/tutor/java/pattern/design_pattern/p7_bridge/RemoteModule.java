package tungnn.tutor.java.pattern.design_pattern.p7_bridge;

// Abstraction
class RemoteControl {

  protected Device device; // "The Bridge"

  public RemoteControl(Device device) {
    this.device = device;
  }

  public void togglePower() {
    if (device.isEnabled()) {
      device.disable();
    } else {
      device.enable();
    }
  }

  public void volumeDown() {
    device.setVolume(device.getVolume() - 10);
  }

  public void volumeUp() {
    device.setVolume(device.getVolume() + 10);
  }
}

// Refined Abstraction: Điều khiển nâng cao có thêm nút Mute
class AdvancedRemoteControl extends RemoteControl {
  public AdvancedRemoteControl(Device device) {
    super(device);
  }

  public void mute() {
    System.out.println("Remote: Đang tắt tiếng...");
    device.setVolume(0);
  }
}
