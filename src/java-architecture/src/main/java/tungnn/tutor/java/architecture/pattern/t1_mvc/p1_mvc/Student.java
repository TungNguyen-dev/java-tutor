package tungnn.tutor.java.architecture.pattern.t1_mvc.p1_mvc;

import java.util.ArrayList;
import java.util.List;

public class Student {

  private final String id;
  private String name;
  private final List<StudentObserver> observers = new ArrayList<>();

  public Student(String id, String name) {
    this.id = id;
    this.name = name;
  }

  public void addObserver(StudentObserver observer) {
    observers.add(observer);
  }

  public void setName(String name) {
    this.name = name;
    notifyAllObservers(); // Tự động thông báo khi dữ liệu thay đổi
  }

  public String getName() {
    return name;
  }

  public String getId() {
    return id;
  }

  private void notifyAllObservers() {
    for (StudentObserver observer : observers) {
      observer.update(name, id);
    }
  }
}
