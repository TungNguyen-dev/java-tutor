package tungnn.tutor.java.architecture.pattern.t1_mvc.p4_mva;

// StudentModel.java
public class Student {
  private String name;
  private double score;

  public Student(String name, double score) {
    this.name = name;
    this.score = score;
  }

  // Getters và Setters
  public String getName() { return name; }
  public void setName(String name) { this.name = name; }
  public double getScore() { return score; }
  public void setScore(double score) { this.score = score; }
}