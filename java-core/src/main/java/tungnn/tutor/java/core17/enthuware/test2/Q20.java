package tungnn.tutor.java.core17.enthuware.test2;

public class Q20 {

    public static void main(String[] args) {
        B b = new C();
        A a = b;

        if (a instanceof B b1) b1.b();
        if (a instanceof C c1) c1.c();
        if (a instanceof D d1) d1.d();
    }
}

class A {
    void a() {
        System.out.println("a");
    }
}

class B extends A {
    void b() {
        System.out.println("b");
    }
}

class C extends B {
    void c() {
        System.out.println("c");
    }
}

class D extends C {
    void d() {
        System.out.println("d");
    }
}
