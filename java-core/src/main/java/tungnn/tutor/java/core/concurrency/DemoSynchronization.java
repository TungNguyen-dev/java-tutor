package tungnn.tutor.java.core.concurrency;

public class DemoSynchronization {

  private int counter = 0;
  private static int staticCounter = 0;

  // Synchronized statement block
  public void incrementWithSynchronizedBlock() {
    Object lock = new Object();
    synchronized (lock) {
      counter++;
      System.out.println(
          Thread.currentThread().getName() + " (synchronized block): counter = " + counter);
    }
  }

  // Synchronized instance method
  public synchronized void incrementInstanceMethod() {
    counter++;
    System.out.println(
        Thread.currentThread().getName() + " (instance method): counter = " + counter);
  }

  // Synchronized static method
  public static synchronized void incrementStaticMethod() {
    staticCounter++;
    System.out.println(
        Thread.currentThread().getName() + " (static method): staticCounter = " + staticCounter);
  }

  // Example usage with threads
  public static void main(String[] args) {
    DemoSynchronization demo = new DemoSynchronization();

    Runnable instanceTask =
        () -> {
          for (int i = 0; i < 3; i++) {
            demo.incrementInstanceMethod();
            demo.incrementWithSynchronizedBlock();
          }
        };

    Runnable staticTask =
        () -> {
          for (int i = 0; i < 3; i++) {
            DemoSynchronization.incrementStaticMethod();
          }
        };

    Thread t1 = new Thread(instanceTask, "Thread-1");
    Thread t2 = new Thread(instanceTask, "Thread-2");
    Thread t3 = new Thread(staticTask, "Thread-3");
    Thread t4 = new Thread(staticTask, "Thread-4");

    t1.start();
    t2.start();
    t3.start();
    t4.start();
  }
}
