package tungnn.tutor.java.core17.base.expressions;

import java.util.function.Function;

public class PatternMatching {

    public static double getPerimeter(Shape shape) throws IllegalArgumentException {
        if (shape instanceof Rectangle r) {
            return 2 * r.length() + 2 * r.width();
        } else if (shape instanceof Circle c) {
            return 2 * c.radius() * Math.PI;
        } else {
            throw new IllegalArgumentException("Unrecognized shape");
        }
    }

//    public static double getPerimeter1(Shape shape) throws IllegalArgumentException {
//        return switch (shape) {
//            case Rectangle r -> 2 * r.length() + 2 * r.width();
//            case Circle c -> 2 * c.radius() * Math.PI;
//            default -> throw new IllegalArgumentException("Unrecognized shape");
//        };
//    }

    // Guarded
//    static void test(Object obj) {
//        switch (obj) {
//            case String s &&(s.length() == 1) ->System.out.println("Short: " + s);
//            case String s -> System.out.println(s);
//            default -> System.out.println("Not a string");
//        }
//    }

    // Parenthesized
//    static Function<Integer, String> testParen(Object obj) {
//        boolean b = true;
//        return switch (obj) {
//            case (String s && b) -> t -> s;
//            default -> "Default string";
//        };
//    }
}

interface Shape {
}

record Rectangle(double length, double width) implements Shape {
}

record Circle(double radius) implements Shape {
}
