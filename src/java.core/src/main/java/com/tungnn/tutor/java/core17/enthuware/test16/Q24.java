package com.tungnn.tutor.java.core17.enthuware.test16;

public class Q24 {
    static class MyException extends Exception {
    }

    public static void main(String[] args) {
        Q24 tc = new Q24();
        try {
            tc.m1();
        } catch (MyException e) {
            //
            // tc.m1();
        } finally {
            tc.m2();
        }
    }

    public void m1() throws MyException {
        throw new MyException();
    }

    public void m2() throws RuntimeException {
        throw new NullPointerException();
    }
}
