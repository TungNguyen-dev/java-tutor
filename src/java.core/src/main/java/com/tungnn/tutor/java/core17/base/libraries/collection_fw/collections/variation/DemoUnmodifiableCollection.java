package com.tungnn.tutor.java.core17.base.libraries.collection_fw.collections.variation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DemoUnmodifiableCollection {

    public static void main(String[] args) {
        List<String> originalList = new ArrayList<>(
            Arrays.asList("A", "B", "C"));
        List<String> unmodifiableList = List.of("A", "B", "C");
//            Collections.unmodifiableList(originalList);

        System.out.println("Original List: " + originalList);
        System.out.println("Unmodifiable List: " + unmodifiableList);

        // Uncommenting the below line will throw UnsupportedOperationException
        // unmodifiableList.add("D");

        // Modifying the original list is allowed
        originalList.add("D");

        System.out.println("Modified Original List: " + originalList);
        // Reflects the change
        System.out.println(
            "Unmodifiable List After Original Modification: " + unmodifiableList);
    }
}
