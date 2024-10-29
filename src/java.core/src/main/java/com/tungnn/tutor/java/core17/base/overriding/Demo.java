package com.tungnn.tutor.java.core17.base.overriding;

import java.io.IOException;
import java.sql.SQLException;

public class Demo {

    class Super {

        void test() throws IOException {
        }
    }

    class Sub extends Super {

        @Override
        void test() throws IOException {
            super.test();
        }
    }
}
