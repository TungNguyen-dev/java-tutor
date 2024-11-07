package com.tungnn.tutor.java.core17.enthuware.test05;

import java.util.concurrent.atomic.AtomicInteger;

public class Q02 {

    public static void main(String[] args) {
        AtomicInteger atomicInteger = new AtomicInteger(0);

        System.out.println(atomicInteger.getAndIncrement());
        System.out.println(atomicInteger.getAndAdd(1));

        System.out.println(atomicInteger.incrementAndGet());
        System.out.println(atomicInteger.addAndGet(1));
    }
}
