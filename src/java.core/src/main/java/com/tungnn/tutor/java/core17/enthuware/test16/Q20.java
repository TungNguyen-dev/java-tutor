package com.tungnn.tutor.java.core17.enthuware.test16;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Q20 {

    public static void main(String[] args) {
        String sentence1 = "Care diem. Seize the day, boys. " +
                           "Make your lives extra ordinary.";
        String sentence2 = "Frankly, my dear, I don't give a damn!";
        String sentence3 = "Do I look like I give a damn?";

        List<String> sentences = Arrays.asList(sentence1, sentence2, sentence3);

        String rs = sentences
            .stream()
            .flatMap(x -> Arrays.stream(x.split("[ ,.!?\\r\\n]")))
            .distinct()
            .collect(Collectors.joining(" "));

        System.out.println(rs);
    }
}
