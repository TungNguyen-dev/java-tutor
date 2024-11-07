package com.tungnn.tutor.java.core17.base.library.collection.collections;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class CollectionSorting {

    public static void main(String[] args) {
        Object[] objects = new Object[]{100, 200, 50};
        List<Object> objectList = Arrays.asList(objects);

        // If comparator is null --> Sort by `natural order`
        Collections.sort(objectList, null);
        System.out.println(objects[0] + " " + objects[1] + " " + objects[2]);
    }
}
