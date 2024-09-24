package tungnn.tutor.java.core17.base.local_var_type_inference;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.BiFunction;

public class Demo {

    /*
     * Example for Local Variable Type Inference
     */
    public static void main(String[] args) throws IOException {
        // Cannot declare with null initializer
        // var inferredVar1 = null;

        // Example
        var url = new URL("https://www.oracle.com/");
        var conn = url.openConnection();
        var reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));

        // Local variable declarations with initializers:
        var list = new ArrayList<String>();    // infers ArrayList<String>
        var stream = list.stream();            // infers Stream<String>
        var path = Paths.get("fileName");        // infers Path
        var bytes = Files.readAllBytes(path);  // infers bytes[]

        // Enhanced for-loop indexes:
        List<String> myList = Arrays.asList("a", "b", "c");
        for (var element : myList) {
            System.out.println(element);
        }  // infers String

        // Index variables declared in traditional for loops:
        for (var counter = 0; counter < 10; counter++) {
            System.out.println(counter);
        }   // infers int

        // try-with-resources variable:
        try (var input =
                     new FileInputStream("validation.txt")) {
            System.out.println(input.read());
        }   // infers FileInputStream

        // Lambda expressions
        BiFunction<Integer, Integer, Integer> lambdaExpression = (a, b) -> a + b;
    }
}
