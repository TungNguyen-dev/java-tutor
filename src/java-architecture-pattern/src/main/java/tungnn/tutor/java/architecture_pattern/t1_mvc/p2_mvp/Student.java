package tungnn.tutor.java.architecture_pattern.t1_mvc.p2_mvp;

public class Student {

  private String id;
  private String name;

  public Student(String id, String name) {
    this.id = id;
    this.name = name;
  }

  // Getters và Setters
  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }
}
