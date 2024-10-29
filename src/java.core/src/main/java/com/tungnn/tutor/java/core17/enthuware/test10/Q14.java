package com.tungnn.tutor.java.core17.enthuware.test10;

public class Q14 {

    public static void main(String[] args) {
        /*
         * "|" is not short-circuit
         */
        int i = 1;
        int j = i++;
        if ((i == ++j) | (i++ == j)) {
            i += j;
        }
        System.out.println(i);
    }
}
