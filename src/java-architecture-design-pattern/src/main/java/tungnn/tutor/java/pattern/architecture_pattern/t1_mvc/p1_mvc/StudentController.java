package tungnn.tutor.java.pattern.architecture_pattern.t1_mvc.p1_mvc;

public class StudentController {

  private final Student model;

  public StudentController(Student model) {
    this.model = model;
  }

  public void updateStudentName(String name) {
    model.setName(name);
  }
}
