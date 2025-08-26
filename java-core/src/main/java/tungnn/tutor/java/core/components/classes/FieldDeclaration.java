package tungnn.tutor.java.core.components.classes;

public class FieldDeclaration {

  interface A {
    int a = 0;
  }

  interface B {
    long a = 1;
  }

  class Foo implements A, B {
    void method() {
      System.out.println(A.a);
      System.out.println(B.a);
      // Cannot access inherited fields directly if there is a name conflict
      // System.out.println(a);
    }
  }
}
