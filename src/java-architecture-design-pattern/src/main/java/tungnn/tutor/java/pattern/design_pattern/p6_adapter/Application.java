package tungnn.tutor.java.pattern.design_pattern.p6_adapter;

public class Application {

  public static void main(String[] args) {
    // Tạo một con gà tây thực thụ
    WildTurkey turkey = new WildTurkey();

    // Bọc con gà tây vào một cái Adapter để nó trông như một con vịt
    Duck turkeyAdapter = new TurkeyAdapter(turkey);

    System.out.println("The Turkey says...");
    turkey.gobble();
    turkey.fly();

    System.out.println("\nThe TurkeyAdapter says (as a Duck):");
    testDuck(turkeyAdapter);
  }

  // Client chỉ chấp nhận kiểu Duck
  static void testDuck(Duck duck) {
    duck.quack();
    duck.fly();
  }
}
