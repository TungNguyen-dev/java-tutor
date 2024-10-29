package com.tungnn.tutor.java.core17.enthuware.test16;

public class Q34 {

    public static void main(String[] args) {
        /*
         * Invalid code.
         */
//        var c = 0;
//        JACK:
//        while (c < 8) {
//            JILL:
//            System.out.println(c);
//            if (c > 3) break JILL;
//            else c++;
//        }

//        var c = 0;
//        JACK:
//        while (c < 8) {
//            JILL:
//            {
//                System.out.println(c);
//                if (c > 3)
//                    break JILL;
//                else
//                    c++;
//            }
//        }

        var i = 0;
        while (true) {
            block:
            {
                if (i > 8) {
                    break block;
                }
            }
            i++;
            System.out.println(i);
        }
    }
}
