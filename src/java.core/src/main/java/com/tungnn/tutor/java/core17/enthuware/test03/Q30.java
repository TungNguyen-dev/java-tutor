package com.tungnn.tutor.java.core17.enthuware.test03;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;

public class Q30 {

    public static void main(String[] args) {
        AtomicInteger ai = new AtomicInteger();
        Stream.of(11, 11, 22, 33).parallel().forEach(i -> ai.incrementAndGet());
//        stream.filter(e -> {
//            ai.incrementAndGet();
//            return e % 2 == 0;
//        });
        System.out.println(ai);
    }
}
