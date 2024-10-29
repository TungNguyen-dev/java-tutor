package com.tungnn.tutor.java.core17.enthuware.test03;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Q02 {

    public static void main(String[] args) {
        List<String> names = Arrays.asList("Alice", "Bob", "Charlie");

        double averageLength = names.stream().collect(
            Collectors.averagingInt(String::length));
        System.out.println(averageLength);

        double averagePrice = names.stream().collect(
            Collectors.averagingDouble(String::length));
        System.out.println(averagePrice);

        double averageSalary = names.stream().collect(
            Collectors.averagingLong(String::length));
        System.out.println(averageSalary);
    }
}
