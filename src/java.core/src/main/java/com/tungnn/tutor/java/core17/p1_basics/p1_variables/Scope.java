package com.tungnn.tutor.java.core17.p1_basics.p1_variables;

public class Scope {

    /*
     * Static Variable - Static Field
     * Belong to class
     */
    static int staticVar = 0;

    /*
     * Instance Variable - Instance Field
     * Belong to object
     */
    int instanceVar = 0;

    /**
     *
     * @param paramVar Parameter Variable - Like Local variable but it is passed
     */
    void foo(int paramVar) {
        /*
         * Local variable
         * Available in block scope that declare this var
         */
        int localVar = 0;
    }
}
