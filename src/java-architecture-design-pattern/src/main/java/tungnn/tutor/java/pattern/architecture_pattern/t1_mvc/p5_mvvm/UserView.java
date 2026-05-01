package tungnn.tutor.java.pattern.architecture_pattern.t1_mvc.p5_mvvm;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class UserView implements PropertyChangeListener {
  private UserViewModel viewModel;

  public UserView(UserViewModel viewModel) {
    this.viewModel = viewModel;
    // View đăng ký "lắng nghe" ViewModel
    this.viewModel.addPropertyChangeListener(this);
  }

  // Cơ chế tự động cập nhật khi ViewModel phát tín hiệu
  @Override
  public void propertyChange(PropertyChangeEvent evt) {
    if ("displayName".equals(evt.getPropertyName())) {
      render(evt.getNewValue().toString());
    }
  }

  public void render(String name) {
    System.out.println("[VIEW] Giao diện hiển thị tên mới: " + name);
  }

  // Giả lập thao tác người dùng nhập liệu
  public void userActionChangeName(String input) {
    System.out.println("[VIEW] Người dùng nhập: " + input);
    viewModel.updateUserName(input);
  }
}
