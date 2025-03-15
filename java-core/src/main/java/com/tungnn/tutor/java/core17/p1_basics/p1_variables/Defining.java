package com.tungnn.tutor.java.core17.p1_basics.p1_variables;

public class Defining {

    public static void main(String[] args) {
        // Step 1: Declare a variable
        int i;

        // Step 2: Initializing value to that variable
        i = 1;

        // Combine two-steps;
        int j = 1;
    }

    static class Initializing {
        static class DefaultValue {
            // \u0000
            char c;

            // 0
            byte b;
            short s;
            int i;
            long l;

            // 0.0
            float f;
            double d;
            boolean bool;

            // null
            Object o;
        }

        static class LiteralValue {
            char c = '\u0000';

            int i = 128;
            long l = 128L;

            double d = 128.0;
            float f = 128.0f;

            boolean bool = true;
        }

        static class OtherVariable {
            byte b = 127;
            int i = b;

            long l = 127L;
            int j = (int) l;
        }
    }
}
