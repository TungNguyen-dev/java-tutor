package tungnn.tutor.java.core17.base.exceptions;

public class Demo {

    public static void main(String[] args) {
        try {
            System.out.println("Hello World");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            System.out.println("finally");
        }
    }
}
