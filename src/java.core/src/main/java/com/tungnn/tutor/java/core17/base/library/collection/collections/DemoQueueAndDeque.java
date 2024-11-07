package com.tungnn.tutor.java.core17.base.library.collection.collections;

import java.util.ArrayDeque;
import java.util.Deque;

public class DemoQueueAndDeque {

    public static void main(String[] args) {
        demoDeque();
    }

    static void demoQueue() {

    }

    static void demoDeque() {
        Deque<Integer> deque = new ArrayDeque<>();

        deque.offerFirst(1);
        deque.offerFirst(2);
        deque.offerFirst(3);

        System.out.println(deque);

        for (int i = 0; i < 3; i++) {
            System.out.println(deque.poll());
        }
    }
}
