package tungnn.tutor.java.architecture.pattern.t1_mvc.p3_hmvc;

// --- MAIN PAGE MODULE (The Parent) ---
class MainPageController {
  // HMVC giữ các tham chiếu đến các Controller con
  private final CartController cartController = new CartController();
  private final ContentController contentController = new ContentController();

  public void renderPage() {
    System.out.println("=== KHỞI TẠO TRANG CHỦ ===");

    // Gọi các module con để hoàn thiện trang web
    cartController.display();
    contentController.display();

    System.out.println("=== KẾT THÚC TRANG CHỦ ===");
  }
}
