package com.tungnn.tutor.java.core17.base.oop.principles.inheritance.covariant;

public class DemoArray {

    class Super {

        public Number[] test() {
            return new Number[10];
        }
    }

    class Sub extends Super {

        @Override
        public Integer[] test() {
            return new Integer[10];
        }
    }
}
