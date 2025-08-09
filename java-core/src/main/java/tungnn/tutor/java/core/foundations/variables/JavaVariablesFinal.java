package tungnn.tutor.java.core.foundations.variables;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.rmi.AccessException;

public class JavaVariablesFinal {

  void test_FinalVariable_Explicitly() {
    // Have initializers
    final int x = 10;
    // x = 10; // Cannot change value

    // No initializers
    final int y;
    y = 20; // Can change value one time
    // y = 20; // Cannot change value
  }

  void test_FinalVariable_Implicitly() throws IOException {
    /*
     * Interface field.
     * Because it is a public-static-final variable
     */
    interface Foo {
      int i = 1;
      // i = 2; // Compile error
    }

    /*
     * Local parameter in try-with-resources: reader
     */
    try (BufferedReader reader = new BufferedReader(new FileReader("file.txt"))) {
      // reader = null; // Compile error
    }

    /*
     * Multi-catch exception parameter: e
     */
    try (BufferedReader reader = new BufferedReader(new FileReader("file.txt"))) {
    } catch (AccessException | FileNotFoundException e) {
      // e = null; // Compile error
    }
  }

  void test_FinalVariable_Effectively() {
    /*
     * Effectively final variable: i
     * Because: variable not re-assigned or perform ++, --
     */
    int i = 10;
    System.out.println(i);

    // i = 20;
    // ++i++;
    // --i--
  }
}
