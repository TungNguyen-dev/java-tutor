package tungnn.tutor.java.pattern.design_pattern.p6_adapter;

// Adapter Class
public class TurkeyAdapter implements Duck {
  private WildTurkey turkey;

  public TurkeyAdapter(WildTurkey turkey) {
    this.turkey = turkey;
  }

  @Override
  public void quack() {
    // Chuyển đổi yêu cầu quack() thành gobble()
    turkey.gobble();
  }

  @Override
  public void fly() {
    // Gà tây bay ngắn hơn vịt, nên ta cho nó bay 5 lần để bù đắp quãng đường
    for (int i = 0; i < 5; i++) {
      turkey.fly();
    }
  }
}
