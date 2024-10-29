package com.tungnn.tutor.java.core17.base.variable;

public class DemoIntegerChar {

    public static void main(String[] args) {
        int a = 65;
        char c = 'A';

        if (a == c) {
            System.out.println("Interchangeable");
        } else {
            System.out.println("Not Interchangeable");
        }

        /*
         * Unicode code point have format: U+<4 hex number>
         * Example: U+0041 --> 'A'
         */
        int hexInt = 0x0041;
        System.out.println(hexInt);

        // Invalid
        // byte b = c;

        // Valid
        byte b = 'A';
        System.out.println(b);
    }
}
