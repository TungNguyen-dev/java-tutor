package com.tungnn.tutor.java.core17.base.essentials.concurrency.operations;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ConcurrentModificationException {

    public static void main(String[] args) {
        Collection<String> collection = new ArrayList<String>(List.of("a", "b", "c"));

        for (String s : collection) {
            collection.removeIf(e -> e.equals("a"));
        }
    }
}
