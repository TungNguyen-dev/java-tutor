package tungnn.tutor.java.core17.base.records;

public class Demo {

    public static void main(String[] args) {

    }
}

record Example(int a, int b) {

    public Example {
        if (a > b) {
            throw new IllegalArgumentException("a must be greater than b");
        }
    }
}

class Super {
    public Super() {}
}

class Sub extends Super {

    public Sub() {
        throw new IllegalArgumentException("sub must be greater than b");
    }
}