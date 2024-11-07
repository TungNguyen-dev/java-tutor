package com.tungnn.tutor.java.core17.base.essentials.io.nio;

import java.nio.file.Path;

public class PathStartWith {

    public static void main(String[] args) {
        Path path = Path.of("/var/www/html");

        // startsWith convert to string and check
        System.out.println(path.startsWith("/"));
        System.out.println(path.startsWith("var"));
    }
}
