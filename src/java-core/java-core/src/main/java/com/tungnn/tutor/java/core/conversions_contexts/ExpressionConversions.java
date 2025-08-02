package com.tungnn.tutor.java.core.conversions_contexts;

import java.util.ArrayList;
import java.util.List;

public class ExpressionConversions {

  static class Super {

    public Super() {
      System.out.println("Super constructor");
    }

    public void test_Super() {
      System.out.println("Super method");
    }

    public static void test_Super_Static() {
      System.out.println("Super static method");
    }
  }

  static class Sub extends Super {}

  static class ChildSub extends Sub {}

  public static void test_Conversions_Identity() {
    int i = 0;
    int sameInt = (int) i;
  }

  public static void test_Conversions_Widening_Primitive() {
    byte b = 100;
    short s = b; // byte to short
    int i1 = b; // byte to int
    long l1 = b; // byte to long
    float f1 = b; // byte to float
    double d1 = b; // byte to double

    int i2 = s; // short to int
    long l2 = s; // short to long
    float f2 = s; // short to float
    double d2 = s; // short to double

    char c = 'A';
    int i3 = c; // char to int
    long l3 = c; // char to long
    float f3 = c; // char to float
    double d3 = c; // char to double

    int i4 = 100;
    long l4 = i4; // int to long
    float f4 = i4; // int to float
    double d4 = i4; // int to double

    long l5 = 100L;
    float f5 = l5; // long to float
    double d5 = l5; // long to double

    float f6 = 100.0f;
    double d6 = f6; // float to double
  }

  public static void test_Conversions_Narrowing_Primitive() {
    short s = 100;
    byte b1 = (byte) s; // short to byte
    char c1 = (char) s; // short to char

    char c = 'A';
    byte b2 = (byte) c; // char to byte
    short s2 = (short) c; // char to short

    int i = 100;
    byte b3 = (byte) i; // int to byte
    short s3 = (short) i; // int to short
    char c3 = (char) i; // int to char

    long l = 100L;
    byte b4 = (byte) l; // long to byte
    short s4 = (short) l; // long to short
    char c4 = (char) l; // long to char
    int i4 = (int) l; // long to int

    float f = 100.0f;
    byte b5 = (byte) f; // float to byte
    short s5 = (short) f; // float to short
    char c5 = (char) f; // float to char
    int i5 = (int) f; // float to int
    long l5 = (long) f; // float to long

    double d = 100.0;
    byte b6 = (byte) d; // double to byte
    short s6 = (short) d; // double to short
    char c6 = (char) d; // double to char
    int i6 = (int) d; // double to int
    long l6 = (long) d; // double to long
    float f6 = (float) d; // double to float
  }

  public static void test_Conversions_Widening_And_Narrowing_Primitive() {
    byte b = 65;
    char c = (char) b; // byte to char requires both widening and narrowing
  }

  public static void test_Conversions_Widening_Reference() {
    ChildSub properSub = new ChildSub();
    Sub sub = properSub;
    Super aSuper = sub;
    Super aSuper1 = properSub;
  }

  public static void test_Conversions_Narrowing_Reference() {
    Super aSuper = new Super();
    Sub sub = (Sub) aSuper;
    ChildSub properSub = (ChildSub) sub;
  }

  public static void test_Conversions_Boxing() {
    boolean b = true;
    Boolean booleanObj = b;

    byte byteValue = 1;
    Byte byteObj = byteValue;

    short shortValue = 2;
    Short shortObj = shortValue;

    char charValue = 'a';
    Character charObj = charValue;

    int intValue = 3;
    Integer intObj = intValue;

    long longValue = 4L;
    Long longObj = longValue;

    float floatValue = 5.0f;
    Float floatObj = floatValue;

    double doubleValue = 6.0;
    Double doubleObj = doubleValue;
  }

  public static void test_Conversions_Unboxing() {
    Boolean booleanObj = Boolean.TRUE;
    boolean b = booleanObj;

    Byte byteObj = 1;
    byte byteValue = byteObj;

    Short shortObj = 2;
    short shortValue = shortObj;

    Character charObj = 'a';
    char charValue = charObj;

    Integer intObj = 3;
    int intValue = intObj;

    Long longObj = 4L;
    long longValue = longObj;

    Float floatObj = 5.0f;
    float floatValue = floatObj;

    Double doubleObj = 6.0;
    double doubleValue = doubleObj;
  }

  public static void test_Conversions_Unchecked() {
    // Raw type to parameterized type conversion
    List raw = new ArrayList();
    List<String> stringList = raw;

    // Parameterized type to raw type conversion
    List<Integer> intList = new ArrayList<>();
    List rawList = intList;
  }

  public static void test_Conversions_Capture() {
    class WildcardError {
      public void foo(List<?> list) {
        /*
        ExpressionConversions.java:215: error: incompatible types: Object cannot be converted to CAP#1
          list.set(0, list.getFirst());
        where CAP#1 is a fresh type-variable: CAP#1 extends Object from capture of ?
         */
        Object obj = list.getFirst();
        // list.set(0, obj);
      }
    }
    class WildcardFixed {
      public void foo(List<?> list) {
        fooHelper(list);
      }

      // Helper method created so that the wildcard can be captured through type inference.
      private <T> void fooHelper(List<T> list) {
        T obj = list.getFirst();
        list.set(0, obj);
      }
    }
  }

  public static void test_Conversions_String() {
    // Primitive to String conversion
    int intValue = 42;
    String strFromInt = String.valueOf(intValue);
    String strFromIntConcat = "" + intValue;

    double doubleValue = 3.14;
    String strFromDouble = String.valueOf(doubleValue);

    boolean boolValue = true;
    String strFromBool = String.valueOf(boolValue);

    // Object to String conversion
    Integer integerObj = 100;
    String strFromInteger = integerObj.toString();

    // String to primitive conversion
    String numStr = "42";
    int parsedInt = Integer.parseInt(numStr);

    String doubleStr = "3.14";
    double parsedDouble = Double.parseDouble(doubleStr);

    String boolStr = "true";
    boolean parsedBool = Boolean.parseBoolean(boolStr);

    // String to Object conversion
    String integerStr = "100";
    Integer parsedInteger = Integer.valueOf(integerStr);
  }

  public static void test_Conversions_Forbidden() {
    //     These would cause compilation errors:
    //     boolean b = 1;              // int to boolean not allowed
    //     int i = true;              // boolean to int not allowed
    //     float f = false;           // boolean to float not allowed
    //     boolean b2 = 1.0;          // double to boolean not allowed
    //     String s = 'c';            // char to String not allowed without conversion
    //
    //     These would cause compilation errors:
    //     String str = new Object(); // Cannot convert Object to String
    //     Integer i = new Object(); // Cannot convert Object to Integer
    //     ArrayList<Integer> intList = new ArrayList<String>(); // Incompatible generic types
    //
    //     These would cause compilation errors:
    //     Integer i = true;         // Cannot box boolean to Integer
    //     Boolean b = 1;           // Cannot box int to Boolean
    //     int i = new Boolean(true); // Cannot unbox Boolean to int
    //     boolean b = new Integer(1); // Cannot unbox Integer to boolean
  }
}
