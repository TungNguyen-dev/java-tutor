package com.tungnn.tutor.java.core17.base.libraries.collection_fw.collections.list;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class ListWrapper {

    public static void main(String[] args) {
        // Create an implementation of List such as: ArrayList,...
        List<String> namesList = new ArrayList<>();
        namesList.add("John");
        namesList.add("Jane");
        namesList.add("Jack");
        namesList.add("Bob");
        System.out.println("List Of ArrayList     : " + namesList);

        // Original Array
        String[] names = new String[]{"John", "Jane", "Jack", "Bob"};
        System.out.println("Array Original        : " + Arrays.toString(names));
        System.out.println();

        // Create an unmodifiable-list from array
        List<String> namesListOf = List.of(names);
        System.out.println("List Unmodifiable - Array              : " + namesListOf);

        // Create an unmodifiable-list from another list (a copy)
        List<String> namesListCopy = List.copyOf(namesList);
        System.out.println("List Unmodifiable - List (a copy)      : " + namesListCopy);
        System.out.println();

        // Create a view of List based on array
        List<String> namesAsList = Arrays.asList(names);
        System.out.println("List View - Array     : " + namesAsList);

        // Create a view of List based on another list
        List<String> namesListView = namesList.subList(0, 3);
        System.out.println("List View SubList     : " + namesListView);
        System.out.println();

        // Create unmodifiable-view
        List<String> listUnmodifiableView = Collections.unmodifiableList(namesList);
        System.out.println("List Unmodifiable - List (unmodifiable) : " + listUnmodifiableView);
    }
}
