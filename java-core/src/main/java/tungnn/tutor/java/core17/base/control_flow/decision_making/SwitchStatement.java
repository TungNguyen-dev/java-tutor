package tungnn.tutor.java.core17.base.control_flow.decision_making;

import java.time.DayOfWeek;

public class SwitchStatement {

    public static void main(String[] args) {
        DayOfWeek dayOfWeek = DayOfWeek.SATURDAY;

        switch (dayOfWeek) {
            case SUNDAY:
                System.out.println("Sunday");
            default:
                System.out.println("Monday");
        }
    }
}
