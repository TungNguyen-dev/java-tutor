package com.tungnn.tutor.java.core.concurrency;

public class HappenBeforeDemo {

  static int sharedData = 0;
  static boolean ready = false;

  public static void main(String[] args) throws InterruptedException {
    Object lock = new Object();

    Thread writer =
        new Thread(
            () -> {
              synchronized (lock) { // --- sync block begins (monitor enter)
                sharedData = 42; // [W1] write sharedData
                ready = true; // [W2] write ready
              } // --- sync block ends (monitor exit)
            });

    Thread reader =
        new Thread(
            () -> {
              synchronized (lock) { // --- sync block begins (monitor enter)
                if (ready) { // [R1] read ready
                  System.out.println("sharedData = " + sharedData); // [R2] read sharedData
                } else {
                  System.out.println("Not ready yet.");
                }
              } // --- sync block ends (monitor exit)
            });

    writer.start();
    reader.start();

    writer.join(); // Ensures main thread does not start reader until writer completes
    reader.join();
  }
}
