package tungnn.tutor.java.core.components.classes;

public class FieldDeclaration {

  // Declaration of fields (attributes) without initializers
  String s;

  // Declaration of fields with initializers
  String s1 = "", s2 = "1", s3;

  // Array field declaration with new keyword - Must specify size
  String[] arr = new String[10];

  // Array field declaration with initialize
  String[] arr2 = {"a", "b", "c"};

  void test_hide_fields() {
    /*
     * Hiding fields with same name in subclass
     * With
     */

    class Parent {
      int a = 0;
    }

    interface I {
      int a = 1;
    }

    class Child extends Parent implements I {
      int a = 1;

      void method() {
        System.out.println(a); // Access Child's field
        System.out.println(super.a); // Access Parent's field
        System.out.println(I.a); // Access interface I's field
      }
    }

    Child c = new Child();
    c.method();
  }

  void test_inherit_same_fields() {
    /*
     * Inherited fields with same name
     * With same name, same type: no problem
     * If not same type: must access via Qualified name
     */
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
        // But if not access, it is ok
        // System.out.println(a);
      }
    }

    Foo f = new Foo();
    f.method();
  }
}
