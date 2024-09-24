package tungnn.tutor.java.core17.enthuware.test5;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class Q34 {

    public static void main(String[] args) {
        List<Integer> list = new ArrayList<>();
        list.add(1);
        list.add(2);

//        Consumer consumer = System.out::println;
        Consumer consumer = (Object x) -> System.out.println(x);

        list.forEach(consumer);
    }
}
