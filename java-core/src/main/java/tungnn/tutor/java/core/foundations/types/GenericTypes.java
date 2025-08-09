package tungnn.tutor.java.core.foundations.types;

import java.util.ArrayList;
import java.util.List;

public class GenericTypes {

  public static void main(String[] args) {
    test_TypeVariables_Simple();
    test_TypeVariables_Complex();

    test_ParameterizedTypes_TypeArgument();
    test_ParameterizedTypes_Wildcard_UpperBound();
    test_ParameterizedTypes_Wildcard_LowerBound();

    test_TypeErasure();

    test_Types_Reifiable();

    test_Types_Raw();

    test_Types_Intersection();
  }

  public static void test_TypeVariables_Simple() {
    /*
     * Generic class
     * Type Parameters declaration: <T>
     * Type Parameter: T
     */
    class Foo<T> {
      T t;
    }
  }

  public static void test_TypeVariables_Complex() {
    class MyClass {}
    interface MyInterface {}

    /*
     * Type Parameter: T extends MyClass & MyInterface
     * Bounds: MyClass & MyInterface
     */
    class Foo<T extends MyClass & MyInterface> {
      T t;
    }
  }

  public static void test_ParameterizedTypes_TypeArgument() {
    /*
     * Parameterized type: List<String>
     * TypeArgument: String
     */
    List<String> list = new ArrayList<>();
  }

  public static void test_ParameterizedTypes_Wildcard_UpperBound() {
    /*
     * Parameterized type: List<? extends Number>
     * Upper Bound Wildcard: ? extends Number
     */
    List<? extends Number> list2 = new ArrayList<>();
  }

  public static void test_ParameterizedTypes_Wildcard_LowerBound() {
    /*
     * Parameterized type: List<? super Number>
     * Lower Bound Wildcard: `? super Number`
     */
    List<? super Number> list3 = new ArrayList<>();
  }

  public static void test_TypeErasure() {
    /*
     * Type erasure example:
     * Both List<String> and List<Integer> are represented
     * as List at runtime due to type erasure
     */
    List<String> stringList = new ArrayList<>();
    List<Integer> integerList = new ArrayList<>();

    System.out.println(stringList.getClass() == integerList.getClass()); // prints true
  }

  public static void test_Types_Reifiable() {
    /*
     * Reifiable types examples:
     * - primitives
     * - non-generic types
     * - raw types
     * - unbounded wildcard types
     * - arrays of reifiable types
     */
    int primitive = 42;
    String nonGeneric = "Hello";
    List raw = new ArrayList();
    List<?> unboundedWildcard = new ArrayList<>();
    String[] stringArray = new String[10];
  }

  public static void test_Types_Raw() {
    /*
     * Raw type examples:
     * Using generic type without type parameters
     * Not recommended - only for backward compatibility
     */
    List rawList = new ArrayList(); // raw type
    rawList.add("string"); // unchecked call warning
    rawList.add(42); // can add any type

    // Mixing raw types with parameterized types
    List<String> stringList = new ArrayList<>();
    rawList = stringList; // OK
    stringList = rawList; // unchecked assignment warning
  }

  public static void test_Types_Intersection() {
    interface A {
      void a();
    }
    interface B {
      void b();
    }

    class C implements A, B {
      public void a() {}

      public void b() {}
    }

    /*
     * Intersection type example:
     * `T extends A & B` --> <CAP#1> that implements A and B
     */
    class Foo<T extends A & B> {
      T t;

      public void test() {
        t.a();
        t.b();
      }
    }

    /*
     * In this case, must use `Foo<C>` to parameterize the type Foo.
     * Because <CAP#1> <=> C
     */

    Foo<C> foo = new Foo<>();
    foo.test();
  }
}
