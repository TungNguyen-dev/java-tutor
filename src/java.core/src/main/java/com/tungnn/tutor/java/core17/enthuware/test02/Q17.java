package com.tungnn.tutor.java.core17.enthuware.test02;

public class Q17 {

    public static void main(String[] args) {
        System.out.println(Sub.ID);
    }
}

class Super {
    static String ID = "QBANK";
}

class Sub extends Super {
    static {
        System.out.print("In Sub");
    }
}
