package tungnn.tutor.java.core17.enthuware.test1;

import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Q16 {

    public static void main(String[] args) {
    }

    public Map<String, Long> countWords(Stream<String> wordStream) {
        return wordStream.collect(Collectors.groupingBy(Function.identity(),
                Collectors.counting()));
    }
}
