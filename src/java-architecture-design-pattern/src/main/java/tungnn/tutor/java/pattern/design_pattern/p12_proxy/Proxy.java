package tungnn.tutor.java.pattern.design_pattern.p12_proxy;

class DocumentProxy implements Document {

  private RealDocument realDocument;
  private String fileName;

  public DocumentProxy(String fileName, String initialContent) {
    this.fileName = fileName;
    // Chúng ta có thể khởi tạo đối tượng thật ngay hoặc dùng Lazy Loading
    this.realDocument = new RealDocument(initialContent);
  }

  @Override
  public void displayContent(String userRole) {
    // Bất kỳ ai cũng có thể xem tên file, nhưng xem nội dung thì cần quyền
    System.out.println("[Proxy] Đang truy cập file: " + fileName);
    realDocument.displayContent(userRole);
  }

  @Override
  public void editContent(String userRole, String newContent) {
    // CHẶN: Chỉ ADMIN mới được sửa
    if ("ADMIN".equalsIgnoreCase(userRole)) {
      realDocument.editContent(userRole, newContent);
    } else {
      System.out.println(
          "[BẢO MẬT] Từ chối: Quyền '" + userRole + "' không được phép sửa tài liệu!");
    }
  }
}
