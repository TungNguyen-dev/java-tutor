package tungnn.tutor.java.core.foundations.variables;

public class JavaVariablesInitValue {

  public static void main(String[] args) {
    test_InitialValue_DefaultValues();
  }

  /*
   * Variables have initial values: corresponding-type-default-value
   * - Class variable
   * - Instance variable
   * - Array Components
   */
  static int x;
  int y;
  static int[] intArray = new int[10];

  static void test_InitialValue_DefaultValues() {
    printPrimitiveDefaults();
    printReferenceDefaults();
  }

  static void printPrimitiveDefaults() {
    byte defaultByte = 0;
    short defaultShort = 0;
    int defaultInt = 0;
    long defaultLong = 0L;
    float defaultFloat = 0.0f;
    double defaultDouble = 0.0d;
    boolean defaultBoolean = false;
    char defaultChar = '\u0000';

    System.out.println("Default values for primitive types:");
    System.out.println("byte: " + defaultByte);
    System.out.println("short: " + defaultShort);
    System.out.println("int: " + defaultInt);
    System.out.println("long: " + defaultLong);
    System.out.println("float: " + defaultFloat);
    System.out.println("double: " + defaultDouble);
    System.out.println("boolean: " + defaultBoolean);
    System.out.println("char: " + defaultChar + "(Unicode: \\u0000)");
  }

  static void printReferenceDefaults() {
    String defaultString = null;
    Object defaultObject = null;
    Integer defaultInteger = null;

    System.out.println("\nDefault values for reference types:");
    System.out.println("String: " + defaultString);
    System.out.println("Object: " + defaultObject);
    System.out.println("Integer: " + defaultInteger);
  }

  /*
   * Local variable has got no initial value.
   */
  static void test_InitialValue_LocalVariable() {
    int i;
    // System.out.println(i); // Compilation error: variable i might not have been initialized
  }
}
