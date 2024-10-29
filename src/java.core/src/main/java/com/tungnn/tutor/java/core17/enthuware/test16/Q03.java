package com.tungnn.tutor.java.core17.enthuware.test16;

import java.util.OptionalDouble;
import java.util.stream.IntStream;

public class Q03 {

    public static void main(String[] args) {
        IntStream is1 = IntStream.range(0, 5);

        OptionalDouble x = is1
            .peek(System.out::println)
            .average();

        System.out.println(x);
    }
}
