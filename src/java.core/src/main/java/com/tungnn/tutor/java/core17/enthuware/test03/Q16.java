package com.tungnn.tutor.java.core17.enthuware.test03;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class Q16 {

    public static void main(String[] args) {
        List<String> list = Arrays.asList("a", "b", "c");
        boolean b = list.stream().allMatch(s -> s.equals("a"));
        System.out.println(b);

        Optional<String> optional = list.stream().findFirst();
    }
}
