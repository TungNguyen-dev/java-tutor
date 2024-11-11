package com.tungnn.tutor.java.core17.enthuware.test05;

import java.util.Map;
import java.util.Properties;
import java.util.stream.Collectors;

public class Q45 {

    public static void main(String[] args) {
        Properties props = new Properties();
        Map<String, Object> propMap = props.entrySet()
            .stream()
            .collect(Collectors.toMap(e -> e.getKey().toString(), e -> e.getValue().toString()));

        System.out.println(propMap);
    }
}
