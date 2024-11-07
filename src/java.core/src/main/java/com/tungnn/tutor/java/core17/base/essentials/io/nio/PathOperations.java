package com.tungnn.tutor.java.core17.base.essentials.io.nio;

import java.nio.file.Path;

public class PathOperations {

    public static void main(String[] args) {
        Path path = Path.of("a", "b", "c", "d", "e", "f", "g", "h", "i");

        // Get name by index start by index
        System.out.println(path.getName(1));

        // Sub-path by beginIndex and endIndex
        System.out.println(path.subpath(1, 2));
    }
}
