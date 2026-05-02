package tungnn.tutor.java.pattern.design_pattern.p10_facade;

public class Application {

  static void main() {
    // Khởi tạo các thành phần subsystem
    Projector projector = new Projector();
    Amplifier amp = new Amplifier();
    Screen screen = new Screen();
    PopcornPopper popper = new PopcornPopper();

    // Tạo Facade
    HomeTheaterFacade homeTheater = new HomeTheaterFacade(projector, amp, screen, popper);

    // Sử dụng giao diện đơn giản
    homeTheater.watchMovie("Inception");

    // Khi xem xong
    homeTheater.endMovie();
  }
}
