package com.tungnn.tutor.java.core17.p1_basics.p2_operators;

public class BinaryOperator {

    public static void main(String[] args) {
        arithmeticOperation();
        bitShiftOperation();
        comparisonOperation();
    }

    public static void arithmeticOperation() {
        int a = 20;
        int b = 4;
        int c = 3;

        int result1 = a * b / c; // Multiplication and division are evaluated left to right: (20 * 4) / 3 = 80 / 3 = 26
        int result2 = a % c;     // Modulus operation: 20 % 3 = 2

        System.out.println("Multiplication and Division: " + result1);
        System.out.println("Modulus: " + result2);

        int result3 = a + b - c; // Addition and subtraction are evaluated left to right: (20 + 4) - 3 = 24 - 3 = 21
        int result4 = a + b * c; // Multiplication has higher precedence: 20 + (4 * 3) = 20 + 12 = 32

        System.out.println("Addition and Subtraction: " + result3);
        System.out.println("Mixed Precedence: " + result4);
    }

    public static void bitShiftOperation() {
        // Left Shift (<<)
        int a = 5;  // Binary: 0000 0101
        int result = a << 2; // Shift left by 2: 0001 0100 (Decimal: 20)

        System.out.println("Left Shift (5 << 2): " + result);

        // Signed Right Shift (>>)
        a = 20;  // Binary: 0001 0100
        result = a >> 2; // Shift right by 2: 0000 0101 (Decimal: 5)

        System.out.println("Signed Right Shift (20 >> 2): " + result);

        // Unsigned Right Shift (>>>)
        a = 20;  // Binary: 0001 0100
        result = a >>> 2; // Shift right by 2: 0000 0101 (Decimal: 5)

        System.out.println("Unsigned Right Shift (20 >>> 2): " + result);
    }

    public static void comparisonOperation() {
        // < , >
        int a = 10;
        int b = 5;

        boolean result1 = a < b;  // false (10 is not less than 5)
        boolean result2 = a > b;  // true (10 is greater than 5)

        System.out.println("a < b: " + result1);
        System.out.println("a > b: " + result2);

        // <= , >=
        a = 10;
        b = 10;

        result1 = a <= b; // true (10 is equal to 10)
        result2 = a >= b; // true (10 is equal to 10)

        System.out.println("a <= b: " + result1);
        System.out.println("a >= b: " + result2);

        // instanceof
        Number number = Integer.valueOf("10");
        if (number instanceof Integer i) {
            System.out.println("Number is Integer: " + i);
        }

        // ==, !=
        a = 10;
        b = 5;
        int c = 10;

        result1 = a == b; // false (10 is not equal to 5)
        result2 = a != b; // true (10 is not equal to 5)
        boolean result3 = a == c; // true (10 is equal to 10)

        System.out.println("a == b: " + result1);
        System.out.println("a != b: " + result2);
        System.out.println("a == c: " + result3);
    }
}
