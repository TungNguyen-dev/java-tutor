package tungnn.tutor.java.core17.enthuware.test1;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;

public class Q9 {

    public static void main(String[] args) {
        String[] sa = {"charlie", "bob", "andy", "dave"};

        var ls = new ArrayList<>(Arrays.asList(sa));

        Comparator<String> comparator = (a, b) -> a.compareTo(b);
        ls.sort(comparator);

        System.out.println(sa[0] + " " + ls.get(0));
    }
}
