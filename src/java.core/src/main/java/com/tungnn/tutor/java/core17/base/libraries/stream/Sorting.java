package com.tungnn.tutor.java.core17.base.libraries.stream;

import java.util.Comparator;
import java.util.stream.Stream;

public class Sorting {

    public static void main(String[] args) {
        Stream<Integer> stream = Stream.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
        stream.sorted().forEach(System.out::println);
        stream.sorted(Comparator.comparing(Integer::intValue)).forEach(System.out::println);
    }
}
