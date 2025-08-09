package tungnn.tutor.java.core.foundations.types;

public class JavaTypes {

  public static void test_Types_Primitive() {
    // Integral types: byte, short, int, long, char
    byte b = 100;
    short s = 1000;
    int i = 10000;
    long l = 100000L;
    char c = 'a';

    // Floating-point types: float, double
    float f = 100.0f;
    double d = 100.0;

    // Boolean type: boolean
    boolean bool = true;
  }

  public static void test_Types_Reference() {
    interface MyInterface {}
    class MyClass implements MyInterface {}

    // Class Type
    MyClass myClass = new MyClass();

    // Interface Type
    MyInterface myInterface = new MyClass();

    class Foo<T> {
      // Type Variable
      T t;

      // Array: Type Variable
      T[] tArray;
    }

    // Array: primitive type; reference type
    int[] intArray = new int[10];
    Integer[] integerArray = new Integer[10];
  }
}
