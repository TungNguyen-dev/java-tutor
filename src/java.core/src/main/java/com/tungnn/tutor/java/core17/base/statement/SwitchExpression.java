package com.tungnn.tutor.java.core17.base.statement;

public class SwitchExpression {

    public static void main(String[] args) {
        String s = args[0];

        String rs;
        rs = switch (s) {
            case "1" -> "";
            default -> "0";
        };
        rs = switch (s) {
            case "1" -> {
                yield "1";
            }
            default -> {
                yield "0";
            }
        };
        rs = switch (s) {
            case "1":
                yield "1";
            default:
                yield "0";
        };

        System.out.println(rs);
    }
}
