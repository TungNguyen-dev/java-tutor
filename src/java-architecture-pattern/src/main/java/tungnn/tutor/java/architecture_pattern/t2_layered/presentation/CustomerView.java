package tungnn.tutor.java.architecture_pattern.t2_layered.presentation;

public class CustomerView {

  public void displayMessage(String message) {
    System.out.println("[View Output]: " + message);
  }

  public String getCustomerInput() {
    return "Tùng NN"; // Giả lập dữ liệu nhận từ bàn phím hoặc form
  }
}
