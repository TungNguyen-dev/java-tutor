package com.tungnn.tutor.java.core17.enthuware.test16;

public class Q45 {

    public static void main(String[] args) {

    }
}

interface Foo {
    // Implicit: public static final
    String FOO = "";

    // Implicit: public abstract
    void foo();

    // Implicit: public
    static void bar() {}

    // Implicit: public
    default void bar(int i) {

    }

    // Implicit: public
    private void bar(String s) {

    }
}
