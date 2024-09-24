package tungnn.tutor.java.core17.base.functionals;

import java.util.function.Predicate;

public class LambdaExpressionDemo {

    public static void main(String[] args) {
        // Anonymous Class
        Predicate<Integer> predicate = new Predicate<Integer>() {
            @Override
            public boolean test(Integer integer) {
                return integer % 2 == 0;
            }
        };
        System.out.println(predicate.test(1));

        // Lambda Expressions
        Predicate<Integer> predicate1 = x -> x % 2 == 0;
        Predicate<Integer> predicate2 = (Integer x) -> x % 2 == 0;
        Predicate<Integer> predicate3 = (var x) -> x % 2 == 0;
        Predicate<Integer> predicate4 = x -> {
            return x % 2 == 0;
        };

        System.out.println(predicate1.test(1));
        System.out.println(predicate2.test(2));
        System.out.println(predicate3.test(3));
        System.out.println(predicate4.test(4));
        System.out.println(predicate4.test(4));
    }
}
