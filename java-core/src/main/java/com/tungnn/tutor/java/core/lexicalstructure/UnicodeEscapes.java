package com.tungnn.tutor.java.core.lexicalstructure;

public class UnicodeEscapes {

    public static void main(String[] args) {
        // \u2122 = ™
        System.out.println("\\u2122 = \u2f122 ");
        System.out.println("\\u2122");
        System.out.println("\\\u2122");
        System.out.println("\uuuu0041"); // Compiles and prints 'A'
    }
}
