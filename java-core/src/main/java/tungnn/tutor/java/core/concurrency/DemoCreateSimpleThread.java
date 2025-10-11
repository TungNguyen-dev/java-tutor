package tungnn.tutor.java.core.concurrency;

public class DemoCreateSimpleThread {

  public static void main(String[] args) {
    Runnable task =
        () -> {
          String threadName = Thread.currentThread().getName();
          System.out.println("Hello " + threadName);
        };

    Thread thread = new Thread(task);
    thread.start();

    try {
      thread.join();
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }
}
