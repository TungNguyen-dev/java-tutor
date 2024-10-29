package com.tungnn.tutor.java.core17.enthuware.test16;

import java.util.Collection;
import java.util.List;
import java.util.stream.Stream;

public class Q21 {

    public static void main(String[] args) {
        List<String> list1 = List.of("1", "2");
        List<String> list2 = List.of("a", "b");

        /*
         * <R> Stream<R> flatMap(Function<? super T, ? extends Stream<? extends R>> mapper);
         */
        Stream.of(list1, list2)
              .flatMap(Collection::stream)
              .forEach(System.out::println);
    }
}
