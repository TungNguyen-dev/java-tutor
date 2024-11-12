package com.tungnn.tutor.java.core17.base.libraries.stream;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Stream;

public class DemoCreateStream {

    public static void main(String[] args) {
        // 1. Empty stream
        Stream<String> streamEmpty = Stream.empty();
        System.out.println("Empty stream: " + streamEmpty.count());

        // 2. Create stream using Stream.of()
        Stream<String> stream = Stream.of("a", "b", "c");
        stream.forEach(System.out::print);
        System.out.println();

        // 3. Create stream from array
        String[] strings = {"a", "b", "c"};
        Stream<String> arrayStream1 = Arrays.stream(strings);
        arrayStream1.forEach(System.out::print);
        System.out.println();

        // 4. Create stream from collection
        Collection<String> collection = List.of("a", "b", "c");
        // 4.1. Sequential stream
        Stream<String> sequentialStream = collection.stream();
        sequentialStream.forEach(System.out::print);
        System.out.println();
        // 4.2 Parallel stream
        Stream<String> parallelStream = collection.parallelStream();
        parallelStream.forEach(System.out::print);
        System.out.println();

        // 5. Generate stream --> infinitive
        Stream<Double> generatorStream = Stream.generate(Math::random);
        generatorStream.limit(5).forEach(System.out::println);

        // 6. Iterate stream
        // 6.1. Iterate stream with: seed, next --> infinitive
        Stream<Integer> iteratorStream = Stream.iterate(1, n -> n + 2);
        iteratorStream.limit(5).forEach(System.out::println);

        // 6.2. Iterate with full: seed, hasNext, next
        Stream<Integer> iteratorStream2 = Stream.iterate(1, n -> n < 5, n -> n + 2);
        iteratorStream2.limit(5).forEach(System.out::println);
    }
}
