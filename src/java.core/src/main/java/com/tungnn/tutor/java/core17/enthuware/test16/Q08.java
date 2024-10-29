package com.tungnn.tutor.java.core17.enthuware.test16;

import java.util.Arrays;

public class Q08 {

    static String[] sa = {"a", "aa", "aaa", "aaaa"};

    static {
        Arrays.sort(sa);
    }

    public static void main(String[] args) {
        String search = "";
        if (args.length != 0) search = args[0];
        System.out.println(Arrays.binarySearch(sa, search));
    }
}
