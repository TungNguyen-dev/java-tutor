package com.tungnn.tutor.java.core17.base.essentials.concurrency;

import java.util.concurrent.atomic.AtomicInteger;

public class DemoAtomicInteger {

    private static final AtomicInteger counter = new AtomicInteger(0);

    public static void main(String[] args) {
        // Create two threads that increment the counter
        Thread t1 = new Thread(new CounterTask());
        Thread t2 = new Thread(new CounterTask());

        t1.start();
        t2.start();

        try {
            t1.join();
            t2.join();
        } catch (InterruptedException e) {
            System.out.println(e.getMessage());
        }

        // Print the final counter value
        System.out.println("Final counter value: " + counter.get());
    }

    static class CounterTask implements Runnable {
        @Override
        public void run() {
            for (int i = 0; i < 1000; i++) {
                // Increment the counter atomically
                counter.incrementAndGet();
            }
        }
    }
}
