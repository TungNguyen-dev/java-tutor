package com.tungnn.tutor.java.core17.p1_basics.p1_variables;

public class DataType {

    static class PrimitiveDataTypes {
        // Character
        char c = 'c';

        // Integer
        byte b = 127;
        short s = 32767;
        int i = 42;
        long l = 42;

        // Floating-point
        float f = 3.14f;
        double d = 3.14;

        // Boolean
        boolean bool = true;
    }

    static class ArrayDataType {
        char[] c = {'a'};

        byte[] b = {127};
        short[] s = {32767};
        int[] i = {42};
        long[] l = {42};

        float[] f = {3.14f};
        double[] d = {3.14};

        boolean[] bool = {true};

        Object[] objects = {new Object()};
    }

    static class ReferenceDataType {
        interface Foo {}

        // Object
        Object o = new Object();

        // Interface
        Foo foo = null;
    }

    static class CharIntInterchangeable {
        // int <-- char: Implicit
        int i = 'c';


        // char <-- int: Implicit in range (0 - 65535 | 0x0000 --> 0xFFFF)
        char c = 65535;

        // char <-- int: Explicit out range
        char c2 = (char) 1_000_000;
    }
}
