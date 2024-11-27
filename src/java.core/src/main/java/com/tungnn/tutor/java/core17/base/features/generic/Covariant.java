package com.tungnn.tutor.java.core17.base.features.generic;

import java.util.List;

public class Covariant {

    class Super {

        public List<? extends Number> testReadOnly() {
            return null;
        }

        public List<? super CharSequence> testWriteOnly() {
            return null;
        }

        public List<Number> test1() {
            return null;
        }

        public Number[] test2() {
            return new Integer[0];
        }
    }

    class Sub extends Super {

//        @Override
//        public List<Number> testReadOnly() {
//            return null;
//        }

//        @Override
//        public List<Integer> testReadOnly() {
//            return null;
//        }

//        @Override
//        public List<? extends Integer> testReadOnly() {
//            return null;
//        }

//        @Override
//        public ArrayList<? extends Number> testReadOnly() {
//            return null;
//        }

//        @Override
//        public ArrayList<? extends Integer> testReadOnly() {
//            return null;
//        }

//        @Override
//        public ArrayList<Integer> testReadOnly() {
//            return null;
//        }


//        @Override
//        public List<CharSequence> testWriteOnly() {
//            return null;
//        }

        @Override
        public List<Object> testWriteOnly() {
            return null;
        }

//        @Override
//        public List<? super Number> test1() {
//            return super.test1();
//        }
    }
}
