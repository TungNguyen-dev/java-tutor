package tungnn.tutor.java.core17.enthuware.test1;

import java.time.Duration;
import java.time.LocalDate;
import java.time.Month;
import java.time.Period;

public class Q18 {

    public static void main(String[] args) {
        Period p = Period.between(LocalDate.now(), LocalDate.of(2022,
            Month.SEPTEMBER, 1));
        System.out.println(p);
        Duration d = Duration.between(LocalDate.now(), LocalDate.of(2022,
            Month.SEPTEMBER, 1));
        System.out.println(d);
    }
}
