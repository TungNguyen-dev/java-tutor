package tungnn.tutor.java.core.concurrency;

public class DemoThreadWaiting {
  public static void main(String[] args) {
    simpleWaitSample();
  }

  // Simple sample for wait()
  public static void simpleWaitSample() {
    final Object lock = new Object();

    Thread waitingThread =
        new Thread(
            () -> {
              synchronized (lock) {
                System.out.println("Thread is waiting...");
                try {
                  lock.wait();
                } catch (InterruptedException e) {
                  System.out.println("Thread was interrupted!");
                }
                System.out.println("Thread resumed!");
              }
            });

    Thread notifierThread =
        new Thread(
            () -> {
              try {
                Thread.sleep(1000); // Let the waitingThread start and wait
              } catch (InterruptedException ignored) {
              }
              synchronized (lock) {
                System.out.println("Notifier thread is notifying...");
                lock.notify();
              }
            });

    waitingThread.start();
    notifierThread.start();

    try {
      waitingThread.join();
      notifierThread.join();
    } catch (InterruptedException ignored) {
    }
  }
}
