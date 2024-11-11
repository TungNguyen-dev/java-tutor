package com.tungnn.tutor.java.core17.enthuware.test05;

public class Q52 {

    public static void main(String[] args) {
        int c = 0;
        A:
        for (var i = 0; i < 2; i++) {
            B:
            for (var j = 0; j < 2; j++) {
                C:
                for (var k = 0; k < 3; k++) {
                    c++;
                    if (k > j) break;
                }
            }
        }
        System.out.println(c);
    }
}
