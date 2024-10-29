package com.tungnn.tutor.java.core17.base.classes.nested;

public class OuterClass {

    static String fieldStatic = "Static Field";
    String fieldInstance = "Instance Field";

    public static void main(String[] args) {

    }

    static class InnerStaticClass {

        void testInnerStaticClassMethod() {
            System.out.println(fieldStatic);
        }
    }

    class InnerClass {
        void testInnerInstanceClassMethod() {
            String localField = fieldStatic;
            System.out.println(fieldInstance);
        }
    }

    void outerLocalClassMethod() {
        class LocalClass {
            void testInnerLocalClassMethod() {
                System.out.println(fieldInstance);
            }
        }

        LocalClass localClass = new LocalClass();
        localClass.testInnerLocalClassMethod();
    }

    void outerAnonymousClassMethod() {
        Runnable anonymousClass = new Runnable() {
            public void run() {
                System.out.println(fieldInstance);
            }
        };
        anonymousClass.run();
    }
}
