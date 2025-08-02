package com.tungnn.tutor.java.core.conversions_contexts;

public class ExpressionContexts {

  public static void test_Context_Assignment() {
    // Primitive type assignments
    byte b = 10;
    short s = b; // widening primitive conversion
    int i = s; // widening primitive conversion
    long l = i; // widening primitive conversion
    float f = l; // widening primitive conversion
    double d = f; // widening primitive conversion

    // Narrowing primitive conversion (requires explicit cast)
    int x = (int) l;
    short y = (short) i;
    byte z = (byte) s;

    // String assignments
    String str = "Hello";
    Object obj = str; // widening reference conversion

    // Array assignments
    int[] intArray = new int[5];
    Object objArray = intArray; // array type conversion

    // Compound assignments
    int value = 5;
    value += 3; // compound assignment

    // Boxing/Unboxing assignments
    Integer boxed = 100; // auto-boxing
    int unboxed = boxed; // auto-unboxing
  }

  public static void test_Context_Invocation() {
    class Util {
      public static void printNumber(double num) {
        System.out.println(num);
      }

      public static void printObject(Object obj) {
        System.out.println(obj);
      }

      public static void printPrimitive(int value) {
        System.out.println(value);
      }

      public static void printObjectArray(Object arr) {}
    }
    // Primitive widening
    Util.printNumber(10); // int to double conversion

    // Reference type conversion
    String message = "Hello";
    Util.printObject(message); // String to Object conversion

    // Boxing conversion
    Integer boxedNum = 42;
    Util.printPrimitive(boxedNum); // Integer to int unboxing

    // Array conversion
    int[] numbers = {1, 2, 3};
    Util.printObjectArray(numbers); // array to Object conversion
  }

  public static void test_Context_String() {
    // String concatenation context
    int number = 42;
    String result = "Number: " + number; // int to String conversion
  }

  public static void test_Context_Casting() {
    // Primitive type casting
    double d = 100.5;
    int i = (int) d; // double to int
    byte b = (byte) i; // int to byte

    // Reference type casting
    Object obj = "Hello";
    String str = (String) obj; // Object to String

    // Array type casting
    Object[] objArray = new String[3];
    String[] strArray = (String[]) objArray; // Object[] to String[]

    // Class hierarchy casting
    class Parent {}
    class Child extends Parent {}

    Parent parent = new Child();
    Child child = (Child) parent; // Parent to Child
  }

  public static void test_Context_Numeric() {
    // Numeric promotions in arithmetic operations
    byte b1 = 10;
    byte b2 = 20;
    int result1 = b1 + b2; // bytes are promoted to int

    // Mixed type arithmetic
    int i = 5;
    double d = 2.5;
    double result2 = i + d; // int is promoted to double

    // Integer division
    int x = 10;
    int y = 3;
    double result3 = (double) x / y; // explicit cast for floating-point division

    // Compound arithmetic
    float f1 = 1.5f;
    float f2 = 2.0f;
    float result4 = f1 * f2; // float arithmetic

    // Bitwise operations (numeric context)
    int flags = 0b1100;
    int mask = 0b1010;
    int result5 = flags & mask; // bitwise AND
  }

  public static void test_Context_Testing() {
    // Assertion context conversions
    int actual = 42;
    int expected = 42;
    assert actual == expected; // primitive comparison

    // Object equality testing
    String actualStr = "test";
    String expectedStr = "test";
    assert actualStr.equals(expectedStr); // object equality

    // Numeric precision testing
    double actualDouble = 1.23;
    double expectedDouble = 1.23;
    double delta = 0.001;
    assert Math.abs(actualDouble - expectedDouble) < delta; // floating-point comparison

    // Collection testing
    Integer[] actualArray = {1, 2, 3};
    Integer[] expectedArray = {1, 2, 3};
    assert java.util.Arrays.equals(actualArray, expectedArray); // array comparison
  }
}
