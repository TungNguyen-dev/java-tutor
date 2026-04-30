package tungnn.tutor.java.architecture.pattern.t1_mvc.p5_mvvm;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

public class UserViewModel {
  private User model;
  private String displayName;
  // Cơ chế giúp View lắng nghe sự thay đổi của ViewModel
  private PropertyChangeSupport support = new PropertyChangeSupport(this);

  public UserViewModel(User model) {
    this.model = model;
    this.displayName = model.getName();
  }

  public void addPropertyChangeListener(PropertyChangeListener pcl) {
    support.addPropertyChangeListener(pcl);
  }

  // Logic xử lý khi User thay đổi tên trên giao diện
  public void updateUserName(String newName) {
    String oldName = this.displayName;
    model.setName(newName);
    this.displayName = newName;

    // Phát tín hiệu: "Dữ liệu displayName đã thay đổi rồi!"
    support.firePropertyChange("displayName", oldName, newName);
  }

  public String getDisplayName() {
    return displayName;
  }
}
