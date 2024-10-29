package com.tungnn.tutor.java.core17.enthuware.test16;

import java.time.ZoneId;
import java.time.ZonedDateTime;

public class Q23 {

    public static void main(String[] args) {
        // ZonedDateTime before DST starts (in Eastern Standard Time)
        ZonedDateTime beforeDST = ZonedDateTime.of(2024, 3, 9, 18, 0, 0, 0,
                                                   ZoneId.of("US/Eastern"));
        System.out.println("Before DST: " + beforeDST);

        // ZonedDateTime after DST starts (in Eastern Daylight Time)
        ZonedDateTime afterDST = ZonedDateTime.of(2024, 3, 10, 18, 0, 0, 0,
                                                  ZoneId.of("US/Eastern"));
        System.out.println("After DST: " + afterDST);

        // ZonedDateTime before DST ends (in Eastern Daylight Time)
        ZonedDateTime beforeDSTEnds = ZonedDateTime.of(2024, 11, 2, 18, 0, 0, 0,
                                                       ZoneId.of("US/Eastern"));
        System.out.println("Before DST Ends: " + beforeDSTEnds);

        // ZonedDateTime after DST ends (in Eastern Standard Time)
        ZonedDateTime afterDSTEnds = ZonedDateTime.of(2024, 11, 3, 18, 0, 0, 0,
                                                      ZoneId.of("US/Eastern"));
        System.out.println("After DST Ends: " + afterDSTEnds);
    }
}
