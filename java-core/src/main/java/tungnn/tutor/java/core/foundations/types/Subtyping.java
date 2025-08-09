package tungnn.tutor.java.core.foundations.types;

import java.io.Serializable;

public class Subtyping {

  public static void main(String[] args) {
    test_Subtyping_Primitive();
    test_Subtyping_ClassInterface_NonGenerics();
  }

  public static void test_Subtyping_Primitive() {
    byte b = 10;
    short s = b; // byte to short
    int i = s; // short to int
    long l = i; // int to long
    float f = l; // long to float
    double d = f; // float to double

    char c = 'a';
    int i2 = c; // char to int
  }

  public static void test_Subtyping_ClassInterface_NonGenerics() {
    // Super class
    class SuperClass {}
    // Interface
    interface SuperInterface {}

    // Subclass
    class Sub extends SuperClass implements SuperInterface {}
    Sub sub = new Sub();

    SuperClass superClass = sub;
    SuperInterface superInterface = sub;

    // Interface with explicit extents
    Object obj = sub;
  }

  public static void test_Subtyping_ClassInterface_Generics() {
    // TODO: In further study.
  }

  public static void test_Subtyping_Array_Primitive() {
    // Primitive array
    int[] intArray = new int[10];

    // Demo
    Object obj = intArray;
    Cloneable cloneable = intArray;
    Serializable serializable = intArray;
  }

  public static void test_Subtyping_Array_Types() {
    class Super {}
    class Sub extends Super {}

    Sub[] subArray = new Sub[10];

    // Demo
    Super[] superArray = subArray;
    Object objArray = subArray;
    Cloneable cloneableArray = subArray;
    Serializable serializableArray = subArray;
  }

  public static void test_Subtyping_LUB() {
    // TODO: In further study.
  }

  public static void test_Subtyping_TypeProjection() {
    // TODO: In further study.
  }
}
