package tungnn.tutor.java.core17.base.overloading;

public class Application2 {

    public static void main(String[] args) {
        Object obj = new Object();
        test(obj);

        Super aSuper = new Super();
        test(aSuper);

        Sub aSub = new Sub();
        test(aSub);

        Sub2 aSub2 = new Sub2();
        test(aSub2);

        Sub3 aSub3 = new Sub3();
        test(aSub3);

        /*
         * Get compile error --> ambiguous
         * --> have more than one method those can be run
         */
//         test(null);
    }

    public static void test(Object obj) {
        System.out.println("Object: " + obj);
    }

    public static void test(Super obj) {
        System.out.println("Super " + obj);
    }

    public static void test(Sub obj) {
        System.out.println("Sub " + obj);
    }

    public static void test(Sub2 obj) {
        System.out.println("Sub2 " + obj);
    }

    public static void test(Sub3 obj) {
        System.out.println("Sub3 " + obj);
    }
}

class Super {

}

class Sub extends Super {

}

class Sub2 extends Sub {

}

class Sub3 extends Sub {

}
