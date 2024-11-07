package com.tungnn.tutor.java.core17.base.oop.principles.inheritance.covariant;

import java.util.Collection;
import java.util.List;

public class DemoGeneric {

    class Super {

        Collection<Number> test() {
            return null;
        }
    }

    class Sub extends Super {

//        Cannot use Collection<Integer>
//        @Override
//        Collection<Integer> test() {
//            return null;
//        }

        /*
         * Correct way to override
         */
        @Override
        List<Number> test() {
            return null;
        }
    }
}