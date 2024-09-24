package tungnn.tutor.java.core17.enthuware.test1;

public class Q17 {

    public static void main(String[] args) {
        /*
         * When dividing float, double to zero
         * Will get
         * POSITIVE_INFINITY
         * NEGATIVE_INFINITY
         *
         * Will print "Infinity"
         */
        Float f1 = 10.0f;
        Float f2 = 0.0f;
        Float f3 = null;
        double f = 0.0;
        try {
            f = f1 / f2;
            System.out.println(f);
            f3 = f1 / f2;
        } catch (Exception e) {
            System.out.println("Exception");
        }
        System.out.println(f3.isInfinite());
    }
}
