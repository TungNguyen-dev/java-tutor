package tungnn.tutor.java.core17.base.overloading;

public class Application3 {

    public static void main(String[] args) {
        /*
         * Get error because have more than one method eligible
         */
//        test(1, 1);
    }

    public static void test(int a, double b) {
        System.out.println("int: " + a + ", double: " + b);
    }

    public static void test(double a, int b) {
        System.out.println("int: " + a + ", double: " + b);
    }
}
