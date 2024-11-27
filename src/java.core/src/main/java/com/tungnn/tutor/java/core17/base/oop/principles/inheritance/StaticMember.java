package com.tungnn.tutor.java.core17.base.oop.principles.inheritance;

public class StaticMember {
}

class Super {
    private String foo = "";
}

interface Foo1 {
    String foo = "";
}

class Sub extends Super implements Foo1 {
    private String foo = "";
}