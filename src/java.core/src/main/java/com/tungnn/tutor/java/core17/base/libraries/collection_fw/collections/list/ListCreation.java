package com.tungnn.tutor.java.core17.base.libraries.collection_fw.collections.list;

import java.util.ArrayList;
import java.util.List;

public class ListCreation {

    public static void main(String[] args) {
        String[] names = new String[]{"John", "Jane", "Jack", "Bob"};

        List<String> nameList = new ArrayList<>(List.of(names));

        System.out.println(nameList);
    }
}
