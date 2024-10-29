package com.tungnn.tutor.java.core17.enthuware.test01;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class Q34 {

    public static void main(String[] args) {
        CopyOnWriteArrayList<String> list = new CopyOnWriteArrayList<>();

        // Adding elements
        list.add("A");
        list.add("B");
        list.add("C");

        // Reading elements
        System.out.println("List: " + list);

        // Iterating over the list
        for (String s : list) {
            System.out.println(s);
        }

        // Modifying the list during iteration (won't cause ConcurrentModificationException)
        for (String s : list) {
            if (s.equals("B")) {
                list.add("D");
            }
            System.out.println(s);
        }

        // Final state of the list
        System.out.println("Updated List: " + list);
    }
}
