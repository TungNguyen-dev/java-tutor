package tungnn.tutor.java.core17.base.overloading;

import java.util.Arrays;

public class Application {

    public static void main(String[] args) {
        test('1');
        test(true);

        test((byte) 1);
        test((short) 1);
        test(1);
        test(1L);
        test(1.1F);
        test(1.1);

        test(Byte.valueOf("1"));
        test(Short.valueOf("1"));
        test(Integer.valueOf("1"));
        test(Long.valueOf("1"));
        test(Float.valueOf("1"));
        test(Double.valueOf("1"));

        int[] arr = {1, 2, 3};
        test(arr);
        test(1, 2, 3);
    }

    public static void test(char a) {
        System.out.println("char: " + a);
    }

    public static void test(boolean a) {
        System.out.println("boolean: " + a);
    }

    public static void test(byte a) {
        System.out.println("byte: " + a);
    }

    public static void test(short a) {
        System.out.println("short: " + a);
    }

    public static void test(int a) {
        System.out.println("integer: " + a);
    }

    public static void test(long a) {
        System.out.println("long: " + a);
    }

    public static void test(float a) {
        System.out.println("float: " + a);
    }

    public static void test(double a) {
        System.out.println("double: " + a);
    }

    public static void test(Byte a) {
        System.out.println("Byte: " + a);
    }

    public static void test(Short a) {
        System.out.println("Short: " + a);
    }

    public static void test(Integer a) {
        System.out.println("Integer: " + a);
    }

    public static void test(Long a) {
        System.out.println("Long: " + a);
    }

    public static void test(Float a) {
        System.out.println("Float: " + a);
    }

    public static void test(Double a) {
        System.out.println("Double: " + a);
    }

//    public static void test(int[] arr) {
//        System.out.println("Array: " + Arrays.toString(arr));
//    }

    public static void test(int... arr) {
        System.out.println("Array: " + Arrays.toString(arr));
    }
}