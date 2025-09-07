package tungnn.tutor.java.core;

public class FinallyBlock {

  public static void main(String[] args) {
    System.out.println("Case 1: Normal return");
    testNormal();

    System.out.println("\nCase 2: Exception thrown");
    try {
      testException();
    } catch (Exception e) {
      System.out.println("Caught exception: " + e.getMessage());
    }
  }

  static int testNormal() {
    try {
      System.out.println("In try block");
      return 1;
    } finally {
      System.out.println("In finally block (normal)");
    }
  }

  static void testException() {
    try {
      System.out.println("In try block");
      throw new RuntimeException("Error in try");
    } finally {
      System.out.println("In finally block (exception)");
    }
  }
}
