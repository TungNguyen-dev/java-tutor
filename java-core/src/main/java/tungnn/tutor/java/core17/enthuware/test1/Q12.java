package tungnn.tutor.java.core17.enthuware.test1;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

public class Q12 {

    public static void main(String[] args) {
        String s = "data/input.txt";
        String d = "data/input.txt";

        try {
//            Files.move(Paths.get(s), Paths.get(d));
            Files.move(Paths.get(s), Paths.get(d), StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
