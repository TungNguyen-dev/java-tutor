package com.tungnn.tutor.java.core17.enthuware.test03;

public class Q55 {

    public static void main(String[] args) {
        Days day = Days.MONDAY;
        System.out.println(day.getName());
    }
}

enum Days {
    SUNDAY("SUN"),
    MONDAY("MON"),
    TUESDAY("TUE"),
    WEDNESDAY("WED"),
    THURSDAY("THU"),
    FRIDAY("FRI"),
    SATURDAY("SAT");

    private final String name;

    Days(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
