package com.tungnn.tutor.java.core17.base.io.stream;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class ReadInputFile {

    public static void main(String[] args) {
        Path pathImport = Path.of("import");
        System.out.println(pathImport.toAbsolutePath());

//        try (BufferedReader br = Files.newBufferedReader(pathImport)) {
//                System.out.println(pathImport);
//            String line;
//            while ((line = br.readLine()) != null) {
//                System.out.println(line);
//            }
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
    }
}
