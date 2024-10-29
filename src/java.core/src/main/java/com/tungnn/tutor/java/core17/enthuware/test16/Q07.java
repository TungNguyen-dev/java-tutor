package com.tungnn.tutor.java.core17.enthuware.test16;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

public class Q07 {

    public static void main(String[] args) throws IOException {
        String pathSource = "";
        String pathTarget = "";

        Files.move(Path.of(pathSource), Path.of(pathTarget),
                   StandardCopyOption.REPLACE_EXISTING);
    }
}
