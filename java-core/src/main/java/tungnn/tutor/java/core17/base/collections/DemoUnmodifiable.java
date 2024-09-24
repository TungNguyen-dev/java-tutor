package tungnn.tutor.java.core17.base.collections;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DemoUnmodifiable {

    public static void main(String[] args) {
        List<String> list = new ArrayList<>();
        list.add("A");
        list.add("B");
        list.add("C");

        List<String> other = List.of("A", "B", "C");
        List<String> copy = List.copyOf(list);
        List<String> view = Collections.unmodifiableList(list);
        List<String> view1 = list.subList(0, 3);

        list.set(0, "D");

        System.out.println(list);
        System.out.println(other);
        System.out.println(copy);
        System.out.println(view);
        System.out.println(view1);
    }
}
