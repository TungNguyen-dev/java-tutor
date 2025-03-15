package com.tungnn.tutor.java.core17.p1_basics.p2_operators;

public class UnaryOperator {

    public static void postfixOperator() {
        // expression ++
        int i = 1;
        int rs = i++;
        System.out.println(i);
        System.out.println(rs);

        // expression --
        int i2 = 1;
        int rs2 = i2--;
        System.out.println(i2);
        System.out.println(rs2);

        // Evaluation Order
        int x = 10;
        int result = x++ + x; // What happens here?

        System.out.println("result: " + result); // Output: 21
        System.out.println("x: " + x);           // Output: 11
    }

    public static void unaryOperator() {
        int a = 5;
        int b = 3;

        // Unary plus
        System.out.println("Unary plus: " + (+a));  // Output: 5

        // Unary minus
        System.out.println("Unary minus: " + (-a));  // Output: -5

        // Increment operator (pre-increment)
        System.out.println("Pre-increment: " + (++a));  // Output: 6
        System.out.println("After increment a: " + a);  // Output: 6

        // Decrement operator (post-decrement)
        System.out.println("Post-decrement: " + (b--));  // Output: 3
        System.out.println("After decrement b: " + b);  // Output: 2

        // Logical negation
        boolean isTrue = true;
        System.out.println("Logical negation: " + (!isTrue));  // Output: false
    }

    public static void castingOperator() {
        // Casting primitive
        byte b = (byte) 5000;
        System.out.println(b);

        class Super {
        }

        class Sub extends Super {
        }

        // Casting reference
        Super s = new Sub();
        Sub sub = (Sub) s;
    }
}
