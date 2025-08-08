package tungnn.tutor.java.core.concurrency;

public class ThreadSimpleCreationDemo {

  public static void main(String[] args) throws InterruptedException {
    Runnable task =
        () -> {
          try {
            Thread.sleep(1000);
          } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
          }
          System.out.println("Sub-thread finished");
        };
    Thread thread = new Thread(task);

    thread.start();
    System.out.println("Main thread finished");
  }
}
