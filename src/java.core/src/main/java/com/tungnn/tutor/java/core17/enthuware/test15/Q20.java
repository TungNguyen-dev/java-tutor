package com.tungnn.tutor.java.core17.enthuware.test15;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public class Q20 {

    public static void main(String[] args) {
        LocalDateTime localDateTime = LocalDateTime.now();
        System.out.println(localDateTime);

        Instant instant = Instant.now();
        System.out.println(instant);

        /*
         * Instant class has a truncatedTo method that takes in a
         *
         * TemporalUnit and returns a new Instant with the fields smaller
         * than the passed unit set to zero. For example, if you pass
         * ChronoUnit.DAYS, hours, minutes, seconds, and nano-seconds will be
         *  set to 0 in the resulting Instant. Similarly, System.out.println
         * (Instant.now().truncatedTo(ChronoUnit.MINUTES)); will print
         * 2022-05-15T12:41:00Z because seconds and nano-seconds will be set
         * to 0.  TemporalUnit is an interface and ChronoUnit is a class that
         *  implements this interface and defines constants such as DAYS,
         * MONTHS, and YEARS.
         *
         * FYI, any unit larger than ChronoUnit.DAYS causes the truncatedTo
         * method to throw UnsupportedTemporalTypeException.
         */
        System.out.println(instant.truncatedTo(ChronoUnit.DAYS));
        System.out.println(instant.truncatedTo(ChronoUnit.YEARS));
    }
}
