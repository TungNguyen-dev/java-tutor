package tungnn.tutor.java.core17.enthuware.test1;

import java.util.Optional;

public class Q19 {

    public static void main(String[] args) {
        Optional<Double> optional1 = Optional.of(getPrice("10"));
        Double result = optional1.orElse(20.0);
        System.out.println(result);

        Optional<Double> optional2 = Optional.ofNullable(getPrice("20"));
        Double result2 = optional2.orElseGet(() -> 20.0);
        System.out.println(result2);
    }

    static Double getPrice(String s) {
        return Double.parseDouble(s);
    }
}
