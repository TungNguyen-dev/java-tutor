package tungnn.tutor.java.architecture.pattern.t2_layered;

import tungnn.tutor.java.architecture.pattern.t2_layered.presentation.CustomerDelegate;
import tungnn.tutor.java.architecture.pattern.t2_layered.presentation.CustomerView;

public class Application {

  static void main() {
    // 1. Khởi tạo các thành phần giao diện
    CustomerView view = new CustomerView();
    CustomerDelegate delegate = new CustomerDelegate();

    // 2. Chạy luồng ứng dụng
    view.displayMessage("--- Chào mừng đến với Hệ thống quản lý ---");

    String inputName = view.getCustomerInput();
    view.displayMessage("Đang xử lý đăng ký cho khách hàng: " + inputName);

    // 3. Gọi xuống lớp Delegate để bắt đầu luồng xử lý phân lớp
    delegate.handleRegistration(inputName);

    view.displayMessage("--- Hoàn tất quy trình ---");
  }
}
