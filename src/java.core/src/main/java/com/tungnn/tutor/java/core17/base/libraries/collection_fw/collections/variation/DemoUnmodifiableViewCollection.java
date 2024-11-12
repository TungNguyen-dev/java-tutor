package com.tungnn.tutor.java.core17.base.libraries.collection_fw.collections.variation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class DemoUnmodifiableViewCollection {

    public static void main(String[] args) {
        List<String> originalList = new ArrayList<>(
            Arrays.asList("A", "B", "C", "D", "E"));

        // A view of the list (elements "B", "C", "D")
        List<String> subList = originalList.subList(1, 4);
        List<String> unmodifiableSubList =
            Collections.unmodifiableList(subList);

        System.out.println("Original List: " + originalList);
        System.out.println("Unmodifiable SubList: " + unmodifiableSubList);

        // Uncommenting the below line will throw UnsupportedOperationException
        // unmodifiableSubList.add("Z");

        // Changing the original list reflects in the
        originalList.set(2, "Z");
        // unmodifiable view
        System.out.println("Modified Original List: " + originalList);
        System.out.println(
            "Unmodifiable SubList After Original Modification: " + unmodifiableSubList);
    }
}
