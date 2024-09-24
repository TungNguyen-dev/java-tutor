package tungnn.tutor.java.core17.base.literals;

public class PrimitiveLiterals {

    public static void main(String[] args) {
        /*
         * Integer Literals
         */
        int i1 = 100;      // Decimal
        int i2 = 0b1010;   // Binary
        int i3 = 0x12034;  // Hexadecimal
        int i4 = 010021;   // Octal

        long l1 = i1;
        long l2 = 100L;
        long l3 = 400_000_000_000L;

        byte b1 = (byte) i1;

        short sh1 = (short) i1;

        /*
         * Floating Point literals
         */
        double d1 = 123.123;
        double d2 = 123.123e3;
        double d3 = 0x123;
        double d4 = 123d;

        float f1 = 123.123F;
        float f2 = 1.2e2F;

        char c = '\u1234';
        String s = "Se\u1023";
        System.out.println(c);
        System.out.println(s);
    }
}
