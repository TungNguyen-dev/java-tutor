package com.tungnn.tutor.java.core17.enthuware.test10;

public class Q52 {

    public static void main(String[] args) {
        StringBuilder sb = new StringBuilder();

        sb.append("location");
        sb.append("-");
        sb.append("log message");
        System.out.println(sb);

        /*
         * Removes the characters
         */
        sb.delete(0, sb.length());
        System.out.println(sb);
    }
}
