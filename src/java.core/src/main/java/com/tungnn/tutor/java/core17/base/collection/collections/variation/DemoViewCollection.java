package com.tungnn.tutor.java.core17.base.collection.collections.variation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DemoViewCollection {

    public static void main(String[] args) {
        List<String> originalList = new ArrayList<>(
            Arrays.asList("A", "B", "C", "D", "E"));

        // A view of the
        List<String> subList = originalList.subList(1, 4);

        System.out.println("Original List: " + originalList);
        System.out.println("SubList: " + subList);

        // Changing subList changes the original list
        subList.set(1, "Z");

        System.out.println("Modified Original List: " + originalList);
        System.out.println("Modified SubList: " + subList);
    }
}
