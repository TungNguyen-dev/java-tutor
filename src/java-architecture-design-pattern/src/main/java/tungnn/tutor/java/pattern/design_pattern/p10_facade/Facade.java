package tungnn.tutor.java.pattern.design_pattern.p10_facade;

class HomeTheaterFacade {
  private Projector projector;
  private Amplifier amp;
  private Screen screen;
  private PopcornPopper popper;

  public HomeTheaterFacade(
      Projector projector, Amplifier amp, Screen screen, PopcornPopper popper) {
    this.projector = projector;
    this.amp = amp;
    this.screen = screen;
    this.popper = popper;
  }

  // Phương thức đơn giản hóa thay vì phải gọi hàng chục lệnh lẻ tẻ
  public void watchMovie(String movie) {
    System.out.println("--- Get ready to watch: " + movie + " ---");
    popper.on();
    popper.pop();
    screen.down();
    projector.on();
    projector.wideScreenMode();
    amp.on();
    amp.setVolume(10);
    System.out.println("--- Movie started! Enjoy! ---\n");
  }

  public void endMovie() {
    System.out.println("--- Shutting movie theater down... ---");
    popper.off();
    screen.up();
    projector.off();
    amp.off();
    System.out.println("--- Done! ---");
  }
}
