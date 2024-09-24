package tungnn.tutor.java.core17.enthuware.test5;

import java.time.LocalDate;

import static java.time.DayOfWeek.FRIDAY;

public class Q24 {

    public static void main(String[] args) {
        var day = LocalDate.now().with(FRIDAY).getDayOfWeek();
        switch (day) {
            case MONDAY:
                TUESDAY:
                WEDNESDAY:
                THURSDAY:
                FRIDAY:
                System.out.println("working");
            case SATURDAY:
                SUNDAY:
                System.out.println("off");
        }
    }
}
