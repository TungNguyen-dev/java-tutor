package com.tungnn.tutor.java.core17.base.functional;

import java.util.function.BiFunction;
import java.util.function.BinaryOperator;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;

public class DemoDeclaration {

    public static void main(String[] args) {
        Function<String, Boolean> function = (s) -> !s.isEmpty();
        UnaryOperator<String> unaryOperator = (s1) -> s1.concat("");

        BiFunction<String, String, Boolean> biFunction = (s1, s2) -> !s1.isEmpty();
        BinaryOperator<String> binaryOperator = (s1, s2) -> s1.concat(s2);

        Supplier<String> supplier = () -> "Hello";

        Consumer<String> consumer = (s) -> System.out.println(s.length());

        Predicate<String> predicate = (s) -> !s.isEmpty();
    }
}
