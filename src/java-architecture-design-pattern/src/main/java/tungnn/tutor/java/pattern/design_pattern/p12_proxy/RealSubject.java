package tungnn.tutor.java.pattern.design_pattern.p12_proxy;

class RealDocument implements Document {

  private String content;

  public RealDocument(String content) {
    this.content = content;
  }

  @Override
  public void displayContent(String userRole) {
    System.out.println("Nội dung tài liệu: " + content);
  }

  @Override
  public void editContent(String userRole, String newContent) {
    this.content = newContent;
    System.out.println("Tài liệu đã được cập nhật nội dung mới.");
  }
}
