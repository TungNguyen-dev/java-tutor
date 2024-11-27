package com.tungnn.tutor.java.core17.base.libraries.collection_fw.collections;

import java.util.Arrays;
import java.util.List;
import java.util.function.UnaryOperator;

public class ReplaceAll {

    public static void main(String[] args) {
        List<Integer> list = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9);
        UnaryOperator<Integer> unaryOperator = x -> x * 2;
        list.replaceAll(unaryOperator);
        System.out.println(list);
    }
}
