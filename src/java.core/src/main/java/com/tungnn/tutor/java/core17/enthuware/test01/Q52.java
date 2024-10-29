package com.tungnn.tutor.java.core17.enthuware.test01;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class Q52 {

    public static void main(String[] args) {
        List<Integer> list = Arrays.asList(10, 47, 33, 23);
        Integer max;

        max = list.stream().max(Integer::compare).get();
        System.out.println(max);

        max = list.stream().max(Comparator.comparing(i -> i)).get();
        System.out.println(max);

        max = list.stream().reduce((a, b) -> a > b ? a : b).get();
        System.out.println(max);
    }
}
