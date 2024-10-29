package com.tungnn.tutor.java.core17.enthuware.test03;

import java.nio.file.Path;
import java.nio.file.Paths;

public class Q04 {

    public static void main(String[] args) {
        testRelativePath();
        testAbsolutePath();
        testComplexPath();
    }

    static void testRelativePath() {
        Path basePath = Paths.get("/home/user");
        Path resolvedPath = basePath.resolve("documents/file.txt");

        System.out.println(resolvedPath);
    }

    static void testAbsolutePath() {
        Path basePath = Paths.get("/home/user");
        Path resolvedPath = basePath.resolve("/etc/config");

        System.out.println(resolvedPath);
    }

    static void testComplexPath() {
        Path basePath = Paths.get("/home/user");
        Path resolvedPath = basePath.resolve("../other");

        System.out.println(resolvedPath);
    }
}
