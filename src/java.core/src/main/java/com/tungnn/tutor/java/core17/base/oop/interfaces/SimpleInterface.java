package com.tungnn.tutor.java.core17.base.oop.interfaces;

public interface SimpleInterface {

    // public static final String FOO;
    String FOO = "";

    // public abstract void foo();
    void foo();

    // public default void bar();
    default void bar() {
        System.out.println(FOO);
    }

    // public static void staticBar();
    static void staticBar() {
        System.out.println(FOO);
    }
}
