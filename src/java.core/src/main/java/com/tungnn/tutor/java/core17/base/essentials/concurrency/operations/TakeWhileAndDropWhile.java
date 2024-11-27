package com.tungnn.tutor.java.core17.base.essentials.concurrency.operations;

import java.util.stream.Stream;

public class TakeWhileAndDropWhile {

    public static void main(String[] args) {
        Integer[] array = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
        Stream.of(array).takeWhile(i -> i <= 5).forEach(System.out::print);
        System.out.println();

        Integer[] array2 = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
        Stream.of(array2).dropWhile(i -> i <= 5).forEach(System.out::print);
    }
}
