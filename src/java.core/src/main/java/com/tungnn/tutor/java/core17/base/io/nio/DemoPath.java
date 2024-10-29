package com.tungnn.tutor.java.core17.base.io.nio;

import java.nio.file.Path;

public class DemoPath {

    public static void main(String[] args) {
        String dir = "data/var/www/html";
        Path root = Path.of(dir);

        // Resolve
        String filename = "index.html";
        Path path = root.resolve(filename);
        System.out.println(path);

        // Get name
        System.out.println(path.toAbsolutePath());
        System.out.println(path.getFileName());
        System.out.println(path.getRoot());
        System.out.println(path.getParent());
        System.out.println(path.toAbsolutePath().getName(0));
        System.out.println(path.getNameCount());

        // Relative
        Path path1 = Path.of("/var/data/var");
        Path path2 = Path.of("data/var/www/html");
        System.out.println("Relative path: " + path1.relativize(path2));
    }
}
