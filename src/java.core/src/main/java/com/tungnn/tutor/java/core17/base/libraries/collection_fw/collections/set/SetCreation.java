package com.tungnn.tutor.java.core17.base.libraries.collection_fw.collections.set;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class SetCreation {

    public static void main(String[] args) {
        String[] names = new String[]{"John", "Johnny", "Harry", "Porter"};

        Set<String> set = new HashSet<>(Arrays.asList(names));

        System.out.println(set);
    }
}
