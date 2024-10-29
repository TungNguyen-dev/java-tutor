package com.tungnn.tutor.java.core17.base.concurrency;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

public class DemoSimpleThread {

    public static void main(String[] args) {
        demoRunnable();
        System.out.println();
        demoCallable();
    }

    static void demoRunnable() {
        class MyRunnable implements Runnable {
            @Override
            public void run() {
                System.out.println("Thread started with runnable.");
                System.out.println("Thread name     : " + Thread.currentThread().getName());
                System.out.println("Thread id       : " + Thread.currentThread().getId());
                System.out.println("Thread state    : " + Thread.currentThread().getState());
                System.out.println("Thread priority : " + Thread.currentThread().getPriority());
                System.out.println("Thread ended!");
            }
        }
        Thread thread = new Thread(new MyRunnable());
        thread.start();

        try {
            thread.join();
        } catch (InterruptedException e) {
            System.out.println(e.getMessage());
        }
    }

    static void demoCallable() {
        class MyCallable implements Callable<Integer> {
            @Override
            public Integer call() {
                System.out.println("Thread started with callable.");
                System.out.println("Thread name     : " + Thread.currentThread().getName());
                System.out.println("Thread id       : " + Thread.currentThread().getId());
                System.out.println("Thread state    : " + Thread.currentThread().getState());
                System.out.println("Thread priority : " + Thread.currentThread().getPriority());
                System.out.println("Thread ended!");
                return 0;
            }
        }
        FutureTask<Integer> futureTask = new FutureTask<>(new MyCallable());
        Thread thread = new Thread(futureTask);
        thread.start();

        try {
            Integer result = futureTask.get();
            System.out.println("Result of task  : " + result);
        } catch (InterruptedException | ExecutionException e) {
            System.out.println(e.getMessage());
        }
    }
}
