package tungnn.tutor.java.pattern.design_pattern.p7_bridge;

public class Application {

  public static void main(String[] args) {
    System.out.println("--- Thử nghiệm với TV và Remote cơ bản ---");
    Device tv = new Tv();
    RemoteControl basicRemote = new RemoteControl(tv);
    basicRemote.togglePower();
    basicRemote.volumeUp();

    System.out.println("\n--- Thử nghiệm với Radio và Remote nâng cao ---");
    Device radio = new Radio();
    AdvancedRemoteControl advancedRemote = new AdvancedRemoteControl(radio);
    advancedRemote.togglePower();
    advancedRemote.mute(); // Tính năng chỉ có ở AdvancedRemote
  }
}
