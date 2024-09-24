package tungnn.tutor.java.core17.enthuware.test1;

import java.util.DoubleSummaryStatistics;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Q23 {

    public static void main(String[] args) throws Exception {
        Car[] cars = new Car[]{new SUV("Ford", 5000), new Sedan("Honda",
            10000.0), new SUV("Chevy", 10000), new Sedan("Toyota",
            20000.0)};
        DoubleSummaryStatistics dss =
            Stream.of(cars).filter(c -> c instanceof SUV).collect(Collectors.summarizingDouble(c -> ((SUV) c).milage));
        System.out.println(dss.getMax());
        System.out.println(dss.getCount());
        String sedans =
            Stream.of(cars).filter(c -> c instanceof Sedan).collect(Collectors.mapping(c -> c.name, Collectors.joining(",")));
        System.out.println(sedans);
    }
}

sealed abstract class Car permits SUV, Sedan {
    String name;

    Car(String name) {
        this.name = name;
    }
}

final class SUV extends Car {
    int milage;

    SUV(String name, int milage) {
        super(name);
        this.milage = milage;
    }
}

final class Sedan extends Car {
    double price;

    Sedan(String name, double price) {
        super(name);
        this.price = price;
    }
}
