package tungnn.tutor.java.core17.base.expressions;

import java.time.DayOfWeek;

public class SwitchExpression {

    public static void main(String[] args) {
        DayOfWeek day = DayOfWeek.WEDNESDAY;
//        String s1 = switch (day) {
//            case MONDAY -> "MONDAY";
//            case TUESDAY -> "TUESDAY";
//            case WEDNESDAY -> "WEDNESDAY";
//            case THURSDAY -> "THURSDAY";
//            case FRIDAY -> "FRIDAY";
//            case SATURDAY -> "SATURDAY";
//            case SUNDAY -> "SUNDAY";
//        };
//        System.out.println(s1);
//
//        String s2 = switch (day) {
//            case MONDAY -> {
//                System.out.println("MONDAY");
//                yield "MONDAY";
//            }
//            case TUESDAY -> {
//                System.out.println("TUESDAY");
//                yield "TUESDAY";
//            }
//            case WEDNESDAY -> {
//                System.out.println("WEDNESDAY");
//                yield "WEDNESDAY";
//            }
//            case THURSDAY -> {
//                System.out.println("THURSDAY");
//                yield "THURSDAY";
//            }
//            case FRIDAY -> {
//                System.out.println("FRIDAY");
//                yield "FRIDAY";
//            }
//            case SATURDAY -> {
//                System.out.println("SATURDAY");
//                yield "SATURDAY";
//            }
//            case SUNDAY -> {
//                System.out.println("SUNDAY");
//                yield "SUNDAY";
//            }
//        };
//        System.out.println(s2);

        DayOfWeek day1 = switch (day) {
            case MONDAY:
            case TUESDAY:
            case WEDNESDAY:
            case THURSDAY:
            case FRIDAY:
                System.out.println("Working day");
                yield DayOfWeek.WEDNESDAY;
            case SATURDAY:
            case SUNDAY:
                System.out.println("Weekend");
                yield DayOfWeek.WEDNESDAY;
            default:
                System.out.println("Unknown day");
                yield DayOfWeek.WEDNESDAY;
        };
    }
}
