package com.tungnn.tutor.java.core17.enthuware.test03;

import java.util.Optional;
import java.util.stream.Stream;

public class Q27 {

    public static void main(String[] args) {
        String sentence
            = "Life is a box of chocolates, Forrest. You never know what " +
              "you're gonna get."; // 1

        String[] sentences = sentence.split("[ ,.]");
//        Optional<String> theword = Stream.of(sentences).anyMatch(w -> w.startsWith("g")); // 2

//        System.out.println(theword.get());
    }
}
