package tungnn.tutor.java.core17.enthuware.test5;

import java.nio.file.Path;
import java.nio.file.Paths;

public class Q6 {

    public static void main(String[] args) {
        Path path1 = Paths.get("./data/input.txt");
        Path path2 = Paths.get("data/../data/input.txt");

        System.out.println(path1.normalize().toUri());
        System.out.println(path2.normalize().toUri());
    }
}
