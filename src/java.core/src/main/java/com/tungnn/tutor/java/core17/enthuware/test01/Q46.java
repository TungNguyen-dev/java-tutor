package com.tungnn.tutor.java.core17.enthuware.test01;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class Q46 {

    public static void main(String[] args) {
        List<String> list = new ArrayList<>();
        list.add("a");
        list.add("b");
        list.add("c");

        Stream<String> stream = list.stream();
        stream.map(String::toUpperCase);
        stream.forEach(System.out::println);
    }
}
