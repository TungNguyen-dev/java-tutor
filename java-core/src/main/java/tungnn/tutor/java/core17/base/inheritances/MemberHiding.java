package tungnn.tutor.java.core17.base.inheritances;

public class MemberHiding {

    public static void main(String[] args) {
        Super sub = new Sub();
        System.out.println(sub.name);

        Sub sub1 = new Sub();
        System.out.println(sub1.name);
    }
}

class Super {
    static String staticName = "Super";
    final String name = "Foo";
}

class Sub extends Super implements InterfaceA {
    String name = "Bar";

    void foo() {
        // Reference to 'staticName' is ambiguous, both 'Super.staticName' and 'InterfaceA.staticName' match
        // System.out.println(staticName);
    }
}

interface InterfaceA {
    String staticName = "InterfaceA";
}
