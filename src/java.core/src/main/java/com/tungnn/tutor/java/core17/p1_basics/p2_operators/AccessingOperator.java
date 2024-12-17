package com.tungnn.tutor.java.core17.p1_basics.p2_operators;

public class AccessingOperator {

    public static void main(String[] args) {
        // Array access: []
        int[] intArray = { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10 };
        int element = intArray[0];
        System.out.println("Element is " + element);

        // Member access: `.` --> foo.<member>
        class Foo {
            int anInt;
        }
        Foo foo = new Foo();
        int member = foo.anInt;
        System.out.println("Member is " + member);
    }
}
