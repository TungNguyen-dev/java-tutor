package tungnn.tutor.java.core.foundations.variables;

import java.util.function.Function;

public class JavaVariablesKinds {

  // Class variable
  static int x = 10;

  // Instance variable
  int y = 20;

  // Array variable. ArrayComponents (ElementType) is int.
  int[] intArray = new int[10];

  /*
   * Method parameter.
   * ParameterType: int
   * ParameterName: i
   */
  void test_MethodParameter(int i) {}

  /*
   * Constructor parameter.
   * ParameterType: int
   * ParameterName: i
   */
  JavaVariablesKinds(int i) {}

  void test_Lambda() {
    /*
     * Lambda expression:
     * ParameterType: String
     * ParameterName: s
     * ReturnType: Integer
     * Body: s.length()
     */
    Function<String, Integer> f =
        (s) -> {
          return s.length();
        };
  }

  void test_Exception() {
    /*
     * Exception Parameter: e
     */
    try {
      throw new Exception("test exception");
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  void test_LocalVariable() {
    /*
     * Local Variable: i
     */
    int i = 1;
  }
}
