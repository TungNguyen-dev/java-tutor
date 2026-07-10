package tungnn.tutor.java.core.lib.multithread;

public final class ThreadUtil {

  private ThreadUtil() {}

  public static void sleep(long millis) {
    try {
      Thread.sleep(millis);
    } catch (InterruptedException e) {
      throw new RuntimeException(e);
    }
  }
}
