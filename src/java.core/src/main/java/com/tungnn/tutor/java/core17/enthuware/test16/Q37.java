package com.tungnn.tutor.java.core17.enthuware.test16;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

public class Q37 {

    public static void main(String[] args) {
        Map hm = new ConcurrentHashMap();

        try {
            hm.put(null, "asdf");
            hm.put("aaa", null);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        hm = new HashMap();
        try {
            hm.put(null, "asdf");
            hm.put("aaa", null);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        List list = new ArrayList();
        try {
            list.add(null);
            list.add(null);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        list = new CopyOnWriteArrayList();
        try {
            list.add(null);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
