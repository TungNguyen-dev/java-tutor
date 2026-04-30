package tungnn.tutor.java.architecture.pattern.t1_mvc.p5_mvvm;

public class Application {

  static void main() {
    User model = new User("Gemini");
    UserViewModel viewModel = new UserViewModel(model);
    UserView view = new UserView(viewModel);

    // Hiển thị ban đầu
    view.render(viewModel.getDisplayName());

    // Giả lập người dùng đổi tên
    view.userActionChangeName("Gemini 3 Flash");
  }
}
