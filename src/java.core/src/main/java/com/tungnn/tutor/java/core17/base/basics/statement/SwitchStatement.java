package com.tungnn.tutor.java.core17.base.basics.statement;

public class SwitchStatement {

    public static void main(String[] args) {
        String s = args[0];

        switch (s) {
            case "1":
                System.out.println("1");
                break;
            default:
                System.out.println("default");
                break;
        }

        switch (s) {
            case "1" -> System.out.println("1");
            default -> System.out.println("default");
        }

        switch (s) {
            case "1" -> {
                System.out.println("1");
            }
            default -> {
                System.out.println("default");
            }
        }

//        Invalid syntax
//        switch (s) {
//            case "1":
//                System.out.println("1");
//                break;
//            default -> {
//                System.out.println("default");
//                break;
//            }
//        }
    }
}
