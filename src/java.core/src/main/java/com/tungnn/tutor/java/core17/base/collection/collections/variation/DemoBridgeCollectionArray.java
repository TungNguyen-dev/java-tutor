package com.tungnn.tutor.java.core17.base.collection.collections.variation;

import java.util.Arrays;
import java.util.List;

public class DemoBridgeCollectionArray {

    public static void main(String[] args) {
        String[] strings = {"a", "b", "c"};
        List<String> list = Arrays.asList(strings);

//        list.add("d");
        System.out.println(list);

        String[] strings2 = list.toArray(new String[list.size()]);
        System.out.println(Arrays.toString(strings2));
    }
}
