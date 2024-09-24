package tungnn.tutor.java.core17.base.nested_classes;

import java.util.function.Predicate;

public class Outer {

    static int fieldStatic;
    int fieldInstance;

    static class InnerStatic {
        static int fieldStatic;

        void test() {
            System.out.println(fieldStatic);
            System.out.println(Outer.fieldStatic);
        }
    }

    record InnerRecord(Long id, String name) {

    }

    class InnerInstance {
        void test() {
            System.out.println(fieldInstance);
            System.out.println(Outer.this.fieldInstance);
        }
    }

    void innerLocal() {
        int fieldLocal = 0;

        class InnerLocal {
            void test() {
                System.out.println(fieldLocal);
                System.out.println(fieldInstance);
                System.out.println(Outer.this.fieldInstance);
            }
        }

        InnerLocal innerLocal = new InnerLocal();
        System.out.println(innerLocal);
    }

    void innerAnonymous() {
        Predicate p = new Predicate() {
            @Override
            public boolean test(Object o) {
                return false;
            }

            void test() {
                System.out.println(fieldInstance);
            }
        };

        System.out.println(p);
    }
}
