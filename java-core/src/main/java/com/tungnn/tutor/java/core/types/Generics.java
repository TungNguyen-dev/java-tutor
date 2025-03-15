package com.tungnn.tutor.java.core.types;

public class Generics {

    /*
     *  T  - Type Variable
     * <T> - Type Parameter
     */
    static class Foo<T> {
        public T bar() {
            return null;
        }

        public static void main(String[] args) {
            /*
             * Foo<String> - Parameterized Type
             * String      - Type Argument
             */
            Foo<String> foo = new Foo<>();

            /*
             * ? - Type Argument - Unbounded
             */
            Foo<?> foo1 = new Foo<>();

            /*
             * ? extends Number - Type Argument - Upper Bounded
             */
            Foo<? extends Number> foo2 = new Foo<>();

            /*
             * ? super Number - Type Argument - Lower Bounded
             */
            Foo<? super Number> foo3 = new Foo<>();
        }
    }

    static class Foo1<T> {
        static class Foo2<T> extends Foo<T> {
        }
    }
}
