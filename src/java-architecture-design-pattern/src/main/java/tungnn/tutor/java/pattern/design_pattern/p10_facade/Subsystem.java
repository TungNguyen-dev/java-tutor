package tungnn.tutor.java.pattern.design_pattern.p10_facade;

// Thiết bị 1: Máy chiếu (Projector)
class Projector {
  void on() {
    System.out.println("Projector is ON");
  }

  void wideScreenMode() {
    System.out.println("Projector set to 16:9 aspect ratio");
  }

  void off() {
    System.out.println("Projector is OFF");
  }
}

// Thiết bị 2: Hệ thống âm thanh (Amplifier)
class Amplifier {
  void on() {
    System.out.println("Amplifier is ON");
  }

  void setVolume(int level) {
    System.out.println("Amplifier volume set to " + level);
  }

  void off() {
    System.out.println("Amplifier is OFF");
  }
}

// Thiết bị 3: Màn chiếu (Screen)
class Screen {
  void down() {
    System.out.println("Screen is lowering...");
  }

  void up() {
    System.out.println("Screen is rolling up...");
  }
}

// Thiết bị 4: Máy nổ bỏng ngô (PopcornPopper)
class PopcornPopper {
  void on() {
    System.out.println("Popcorn Popper is ON");
  }

  void pop() {
    System.out.println("Popping popcorn!");
  }

  void off() {
    System.out.println("Popcorn Popper is OFF");
  }
}
