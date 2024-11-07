package com.tungnn.tutor.java.core17.base.library.collection.arrays;

import java.util.Arrays;
import java.util.List;

public class DemoAsList {

    public static void main(String[] args) {
        // Create original array
        String[] strings = {"a", "b", "c"};
        System.out.println(Arrays.toString(strings));

        // Create list-view based on array
        List<String> view = Arrays.asList(strings);
        System.out.println(view);

        // Change array --> effect to --> view
        strings[1] = "d";
        System.out.println(Arrays.toString(strings));
        System.out.println(view);

        // Change view --> effect to --> array
        view.set(1, "b");
        System.out.println(Arrays.toString(strings));
        System.out.println(view);

        // Cannot change size of view
        try {
            view.add("d");
        } catch (Exception e) {
            System.out.println("Exception: " + e + " message: " + e.getMessage());
        }
    }
}
