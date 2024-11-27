package com.tungnn.tutor.java.core17.base.libraries.collection_fw.collections;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class CollectionConverting {

    public static void main(String[] args) {
        Collection<String> collection = List.of("a", "b", "c");

        List<String> list = collection.stream().toList();
        System.out.println(list);

        List<String> list2 = new ArrayList<>(collection);
        list2.add("a");
        System.out.println(list2);
    }
}
