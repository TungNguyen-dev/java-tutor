package tungnn.tutor.java.core17.enthuware.test1;

import java.nio.file.Path;
import java.nio.file.Paths;

public class Q22 {

    public static void main(String[] args) {
        Path inputPath = Path.of("data/input.txt");
        Path outputPath = Paths.get("data/output.txt");

        System.out.println(inputPath.getName(0));
        System.out.println(inputPath.getRoot());
        System.out.println(inputPath.getParent());
        System.out.println(inputPath.getFileName());

        System.out.println(outputPath.toAbsolutePath());
    }
}
