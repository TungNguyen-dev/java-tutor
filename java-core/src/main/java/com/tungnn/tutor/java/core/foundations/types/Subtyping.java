package com.tungnn.tutor.java.core.foundations.types;

public class Subtyping {

  static class A<F1, F2> {}

  static class C<F1, F2> extends A<F1, F2> {}

  static class S1 {}

  static class T1 extends S1 {}

  static class S2 {}

  static class T2 extends S2 {}

  public static void test_subtyping_Class_Interface() {
    C<T1, T2> c = null;

    A<T1, T2> a = c;

    // Using wildcard --> S contain T
    A<? extends S1, T2> a2 = c;
  }
}
